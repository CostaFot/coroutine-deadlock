package com.feelsokman.androidtemplate.deadlocktest

interface RefreshTokenServiceClient {

    suspend fun refreshToken(): String?
}
