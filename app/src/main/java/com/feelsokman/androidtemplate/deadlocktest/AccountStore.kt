package com.feelsokman.androidtemplate.deadlocktest

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountStore @Inject constructor() {

    fun getToken(): String = "i am a token"

}
