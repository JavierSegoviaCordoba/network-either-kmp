package com.javiersc.network.either._config.fakes

import com.javiersc.network.either.Headers
import com.javiersc.network.either._config.readResource
import io.ktor.http.headersOf
import io.ktor.util.toMap

val connection: Pair<String, List<String>> = "connection" to listOf("keep-alive")

val contentType: Pair<String, List<String>> = "content-type" to listOf("application/json")

fun headers(code: Int, file: String? = null): Headers {
    val length =
        when {
            file != null -> readResource(file).length.toString()
            code == 204 -> null
            code == 205 -> "0"
            else -> readResource(code.toResourceJsonFile()).length.toString()
        }

    val contentLength: Pair<String, List<String>>? = length?.let { "content-length" to listOf(it) }

    return if (contentLength != null) headersOf(connection, contentLength, contentType).toMap()
    else headersOf(connection, contentType).toMap()
}

internal fun Int.toResourceJsonFile(): String = toString().first() + "xx.json"
