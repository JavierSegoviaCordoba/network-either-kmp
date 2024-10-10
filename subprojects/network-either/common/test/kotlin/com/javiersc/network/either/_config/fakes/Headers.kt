package com.javiersc.network.either._config.fakes

import com.javiersc.network.either.Headers
import io.ktor.http.headersOf
import io.ktor.util.toMap

val connection: Pair<String, List<String>> = "connection" to listOf("keep-alive")

val contentType: Pair<String, List<String>> = "content-type" to listOf("application/json")

fun headers(code: Int, content: String? = null): Headers {
    val length =
        when {
            content != null -> content.length.toString()
            code == 204 -> null
            code == 205 -> "0"
            else -> getJsonResponse(code).length.toString()
        }

    val contentLength: Pair<String, List<String>>? = length?.let { "content-length" to listOf(it) }

    return if (contentLength != null) headersOf(connection, contentLength, contentType).toMap()
    else headersOf(connection, contentType).toMap()
}
