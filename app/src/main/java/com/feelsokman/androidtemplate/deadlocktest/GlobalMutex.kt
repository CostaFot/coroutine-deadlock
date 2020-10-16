package com.feelsokman.androidtemplate.deadlocktest


import kotlinx.coroutines.sync.Mutex

object GlobalMutex : Mutex by Mutex()

