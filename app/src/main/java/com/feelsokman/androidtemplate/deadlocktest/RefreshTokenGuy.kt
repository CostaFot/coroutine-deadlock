package com.feelsokman.androidtemplate.deadlocktest

import com.feelsokman.androidtemplate.core.coroutine.DispatcherProvider
import com.feelsokman.androidtemplate.extensions.logDebug
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshTokenGuy @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun refreshToken() {
        withContext(dispatcherProvider.io) {
            logDebug { "TAG - Refreshing token" }
            delay(5000)
        }
    }
}
