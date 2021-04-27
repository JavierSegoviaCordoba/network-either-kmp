@file:Suppress("TooGenericExceptionCaught")

package com.javiersc.either.network.ktor

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.buildNetworkFailureRemote
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.utils.isNetworkAvailable
import io.ktor.client.features.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.ResetContent
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.util.toMap
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Transform a request made by a Ktor client to NetworkResponse
 */
@Suppress("FunctionNaming", "FunctionName")
public suspend inline fun <reified F, reified S> NetworkEither(
    request: () -> HttpResponse,
): NetworkEither<F, S> = try {
    request().asNetworkEither()
} catch (throwable: Throwable) {
    throwable.asNetworkEither()
}

@PublishedApi
internal suspend inline fun <reified F, reified S> HttpResponse.asNetworkEither(): NetworkEither<F, S> = with(this) {
    if (content.availableForRead == 0) {
        val statusCode = if (status != NoContent || status != ResetContent) NoContent else status
        buildNetworkSuccess(Unit as S, statusCode.value, headers.toMap())
    } else buildNetworkSuccess(decode(), status.value, headers.toMap())
}

@PublishedApi
internal suspend inline fun <reified F, reified S> ResponseException.serializeAsError(): NetworkEither<F, S> = try {
    with(response) { buildNetworkFailureHttp(decode(), status.value, headers.toMap()) }
} catch (throwable: Throwable) {
    buildNetworkFailureUnknown(throwable)
}

@PublishedApi
internal suspend inline fun <reified T> HttpResponse.decode(): T = Json.decodeFromString(readText())

@PublishedApi
internal suspend inline fun <reified F, reified S> Throwable.asNetworkEither(): NetworkEither<F, S> =
    when (this) {
        is ResponseException -> serializeAsError()
        is UnresolvedAddressException -> localOrfailureRemote()
        is IOException -> localOrfailureRemote()
        else -> buildNetworkFailureUnknown(this)
    }

@PublishedApi
internal fun <F, S> localOrfailureRemote(): NetworkEither<F, S> =
    if (isNetworkAvailable) buildNetworkFailureRemote() else buildNetworkFailureLocal()
