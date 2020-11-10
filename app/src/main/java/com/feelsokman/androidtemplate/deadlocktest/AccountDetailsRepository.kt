package com.feelsokman.androidtemplate.deadlocktest

import javax.inject.Inject

interface AccountDetailsRepository {
    fun isUserSignedIn(): Boolean
    fun getToken(): String?
    fun updateToken(token: String)
    fun logout()
}

class AccountDetailsRepositoryImpl @Inject constructor() : AccountDetailsRepository {

    override fun isUserSignedIn(): Boolean = true
    override fun getToken(): String? = "a token"
    override fun updateToken(token: String) {
        // update it here
    }

    override fun logout() {
        // delete stuff
    }
}
