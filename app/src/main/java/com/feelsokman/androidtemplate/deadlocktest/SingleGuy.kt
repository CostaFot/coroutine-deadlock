package com.feelsokman.androidtemplate.deadlocktest

import com.feelsokman.androidtemplate.extensions.logDebug
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SingleGuy @Inject constructor(
    private val accountStore: AccountStore
) {
    var number = 0

    companion object {
        @OptIn(ObsoleteCoroutinesApi::class)
        private val single = newFixedThreadPoolContext(1, "synchronizationPool")
    }

    fun getAuthToken(): String = blockingGetJwt()

    private fun blockingGetJwt(): String {
        return runBlocking {
            withContext(single) {
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
