package com.pragmadreams.redaktor.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

fun <T> Flow<T>.flowOnDefaultContext() : CommonFlow<T> {
    val context = Job() + Dispatchers.Default
    return this.flowOn(context)
}