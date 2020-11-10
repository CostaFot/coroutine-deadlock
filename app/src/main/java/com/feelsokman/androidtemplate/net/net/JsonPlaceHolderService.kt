package com.feelsokman.androidtemplate.net.net

import com.feelsokman.androidtemplate.deadlocktest.AuthorizationInterceptor
import com.feelsokman.androidtemplate.net.net.model.ApiTodo
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface JsonPlaceHolderService {

    @GET("/todos/{id}")
    @Headers(AuthorizationInterceptor.THIS_REQUIRES_A_TOKEN_TO_WORK)
    suspend fun getTodo(@Path(value = "id") todoId: Int): ApiTodo
}
