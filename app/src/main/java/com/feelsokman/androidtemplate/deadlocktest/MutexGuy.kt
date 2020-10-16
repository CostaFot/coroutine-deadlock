package com.feelsokman.androidtemplate.deadlocktest

import com.feelsokman.androidtemplate.extensions.logDebug
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MutexGuy @Inject constructor(
    private val accountStore: AccountStore
) {

    var number = 0

    private val mutex: Mutex = Mutex()


    suspend fun getAuthToken(): String = blockingMutex()

    private suspend fun blockingMutex(): String {
        return mutex.withLock {
            number++
            logDebug { "Hey, I am trying to get this token $number" }
            refreshToken()
            return@withLock accountStore.getToken()
        }
    }

    private suspend fun refreshToken() {
        delay(5000)
    }

}
