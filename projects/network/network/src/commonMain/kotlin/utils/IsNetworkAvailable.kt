package com.javiersc.either.network.utils

import com.javiersc.either.network.internal.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.head

public val isNetworkAvailable: Boolean
    get() =
        runBlocking {
            kotlin.runCatching { httpClient.head<String>(Constants.DnsUrl) }.isSuccess
        }

private val httpClient = HttpClient()
