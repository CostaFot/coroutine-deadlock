package com.feelsokman.androidtemplate.deadlocktest

import com.feelsokman.androidtemplate.extensions.logDebug
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SingleGuy @Inject constructor(
    private val accountStore: AccountStore,
    private val refreshTokenGuy: RefreshTokenGuy
) {
    var number = 0

    companion object {
        @OptIn(ObsoleteCoroutinesApi::class)
        private val single = newSingleThreadContext("single")
    }

    fun getAuthToken(): String = blockingGetJwt()

    // suspend functions used inside here will not block the thread
    // keeping the thread busy via other means (like a massive for loop) will block
    // As a result calling this method while a refresh is happening will not wait until the refresh is finished
    // that's not really what we want
    private fun blockingGetJwt(): String {
        return runBlocking {
            withContext(single) {
                number++
                logDebug { "TAG - Hey, I am trying to get this token $number" }

                if (shouldRefreshToken) {
                    refreshTokenGuy.refreshToken()
                }
                return@withContext accountStore.getToken()
            }
        }
    }

}
