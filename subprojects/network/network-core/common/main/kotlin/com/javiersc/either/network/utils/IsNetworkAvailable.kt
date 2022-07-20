package com.javiersc.either.network.utils

import com.javiersc.either.network.internal.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import kotlinx.coroutines.runBlocking

public val isNetworkAvailable: Boolean
    get() = runBlocking { runCatching { httpClient.head(urlString = Constants.DnsUrl) }.isSuccess }

private val httpClient = HttpClient()
