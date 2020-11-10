package com.feelsokman.androidtemplate.deadlocktest

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class AuthorizationInterceptor(
    private val getTokenUseCase: GetTokenUseCase
) : Interceptor {

    companion object {
        private const val REQUIRES_TOKEN = "Requires-Token"
        const val THIS_REQUIRES_A_TOKEN_TO_WORK = "$REQUIRES_TOKEN: true"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val headers = chain.request().headers
        if (headers[THIS_REQUIRES_A_TOKEN_TO_WORK] == null || headers["Authorization"] != null) {
            // this request doesn't require a token or already has it, proceed!
            return chain.proceed(chain.request())
        }
        // alright, this request needs a token
        val builder = chain.request().newBuilder()
        // remove the fake header first that was added manually
        builder.removeHeader(THIS_REQUIRES_A_TOKEN_TO_WORK)

        val token: String? = getTokenUseCase()
        return if (token != null) {
            builder.addHeader(name = "Authorization", value = "Bearer $token")
            // all good, move along :)
            chain.proceed(builder.build())
        } else {
            // this request really needed a token but we couldn't get one, abort!
            Response.Builder().apply {
                code(666)
                body("".toResponseBody(null))
                protocol(Protocol.HTTP_2)
                message("Oh-oh, missing token")
                request(chain.request())
            }.build()
        }
    }
}
