package com.feelsokman.androidtemplate.deadlocktest

import com.feelsokman.androidtemplate.extensions.logDebug
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RunBlockingMutexGuy @Inject constructor(
    private val accountStore: AccountStore,
    private val refreshTokenGuy: RefreshTokenGuy
) {
    var number = 0

    fun getAuthToken(): String = blockingGetJwt()

    // will absolutely block the calling thread
    private fun blockingGetJwt(): String {
        return runBlocking {
            GlobalMutex.withLock {
                number++
                logDebug { "TAG - Hey, I am trying to get this token $number" }

                if (shouldRefreshToken) {
                    refreshTokenGuy.refreshToken()
                }
                return@withLock accountStore.getToken()
            }
        }
    }
}
