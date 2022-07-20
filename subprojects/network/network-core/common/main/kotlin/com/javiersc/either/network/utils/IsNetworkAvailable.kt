package com.javiersc.either.network.utils

import com.javiersc.either.network.internal.Constants
import com.javiersc.run.blocking.runBlocking
import io.ktor.client.HttpClient
import io.ktor.client.request.head

public val isNetworkAvailable: Boolean
    get() = runBlocking { runCatching { httpClient.head<String>(Constants.DnsUrl) }.isSuccess }

private val httpClient = HttpClient()
