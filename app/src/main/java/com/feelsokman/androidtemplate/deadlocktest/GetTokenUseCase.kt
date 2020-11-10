package com.feelsokman.androidtemplate.deadlocktest

import com.feelsokman.androidtemplate.core.coroutine.DispatcherProvider
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTokenUseCase @Inject constructor(
    private val accountDetailsRepository: AccountDetailsRepository,
    private val refreshTokenServiceClient: RefreshTokenServiceClient,
    private val dispatcherProvider: DispatcherProvider
) {
    private val mutex = Mutex()

    operator fun invoke(): String? = blockingGetToken()

    private fun blockingGetToken(): String? {
        return runBlocking {
            withContext(dispatcherProvider.io) {
                mutex.withLock {
                    if (!accountDetailsRepository.isUserSignedIn()) {
                        // user is not even signed in (ﾉಠдಠ)ﾉ︵┻━┻
                        return@withContext null
                    }
                    if (shouldRefreshToken()) {
                        val token = refreshTokenServiceClient.refreshToken()
                        if (token != null) {
                            // all good, update the token in our repository
                            accountDetailsRepository.updateToken(token)
                        } else {
                            // we failed, log the user out and delete everything in the account manager
                            accountDetailsRepository.logout()
                            return@withContext null
                        }
                    }
                    // if we reached this point then things are great! ╰(◕ᗜ◕)╯
                    return@withContext accountDetailsRepository.getToken()
                }
            }
        }
    }

    private fun shouldRefreshToken(): Boolean {
        return true
    }

}
