package com.feelsokman.androidtemplate.deadlocktest

import com.feelsokman.androidtemplate.extensions.logDebug
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuspendMutexGuy @Inject constructor(
    private val accountStore: AccountStore,
    private val refreshTokenGuy: RefreshTokenGuy
) {

    var number = 0

    suspend fun getAuthToken(): String = blockingMutex()

    // will not block any thread as it suspends, safe to use in UI
    // in the case of the parent scope being cancelled then this work will be stopped
    private suspend fun blockingMutex(): String {
        return GlobalMutex.withLock {
            number++
            logDebug { "TAG - Hey, I am trying to get this token $number" }
            if (shouldRefreshToken) {
                refreshTokenGuy.refreshToken()
            }
            return@withLock accountStore.getToken()
        }
    }

}
