package com.feelsokman.androidtemplate.deadlocktest

import javax.inject.Inject

class AccountDetailsRepository @Inject constructor() {

    fun isUserSignedIn(): Boolean = true
    fun getToken(): String? = "a token"
    fun updateToken(token: String) {
        // update it here
    }

    fun logout() {
        // delete stuff
    }
}
