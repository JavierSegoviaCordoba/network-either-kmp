package com.javiersc.either.network.ktor._internal

import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf

internal object UnknownOutgoing : OutgoingContent.NoContent() {

    private const val key = "NetworkEither.Failure.Unknown"

    override val contentLength: Long = 0

    override fun toString(): String = key

    override val status: HttpStatusCode = HttpStatusCode(9345, key)

    override val headers: Headers =
        headersOf(networkEitherHeader("Failure.Remote"), jsonContentTypeHeader)
}

internal object RemoteErrorOutgoing : OutgoingContent.NoContent() {

    private const val key = "NetworkEither.Failure.Remote"

    override val contentLength: Long = 0

    override fun toString(): String = key

    override val status: HttpStatusCode = HttpStatusCode(9345, key)

    override val headers: Headers =
        headersOf(networkEitherHeader("Failure.Remote"), jsonContentTypeHeader)
}

private fun networkEitherHeader(key: String) = "NetworkEither" to listOf(key)

private val jsonContentTypeHeader =
    HttpHeaders.ContentType to listOf("${ContentType.Application.Json}")
