package com.javiersc.network.either.utils

import com.javiersc.network.either.internal.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

public suspend fun isNetworkAvailable(): Boolean =
    withContext(Dispatchers.Default) {
        val isAppleOk: Deferred<Boolean> = async { ping(Constants.APPLE_URL) }
        val isGoogleOk: Deferred<Boolean> = async { ping(Constants.GOOGLE_URL) }
        val isOk: Boolean = isAppleOk.await() || isGoogleOk.await()
        isOk
    }

private suspend fun ping(url: String): Boolean {
    val codeResult: Result<Int> = runCatching { httpClient.head(url).status.value }
    val code: Int = codeResult.getOrNull() ?: return false
    val is2xx: Boolean = code in 200..299
    return codeResult.isSuccess && is2xx
}

private val httpClient = HttpClient()
