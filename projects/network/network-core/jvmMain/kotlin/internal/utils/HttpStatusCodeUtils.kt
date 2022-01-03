package com.javiersc.either.network.internal.utils

import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

internal infix fun Int.toHttpStatusCode(message: String): HttpStatusCode {
    return HttpStatusCode.allStatusCodes.find { it.value == this } ?: HttpStatusCode(this, message)
}

@PublishedApi
internal val emptyHeader: Headers
    get() = headersOf("Content-Length", listOf("0"))
