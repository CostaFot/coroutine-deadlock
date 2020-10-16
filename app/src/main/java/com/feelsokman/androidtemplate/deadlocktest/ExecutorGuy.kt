package com.feelsokman.androidtemplate.deadlocktest

import com.feelsokman.androidtemplate.extensions.logDebug
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExecutorGuy @Inject constructor(
    private val accountStore: AccountStore
) {

    var number = 0
    private val executor = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    fun getAuthToken(): String = blockingGetJwt()

    private fun blockingGetJwt(): String {
        return runBlocking {
            withContext(executor) {
                number++
                logDebug { "Hey, I am trying to get this token $number" }

                refreshToken()
                return@withContext accountStore.getToken()
            }
        }
    }

    private suspend fun refreshToken() {
        delay(1000)
    }

}
