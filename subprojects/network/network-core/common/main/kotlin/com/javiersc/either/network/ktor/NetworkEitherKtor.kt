@file:Suppress("TooGenericExceptionCaught")

package com.javiersc.either.network.ktor

import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.buildNetworkFailureRemote
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.utils.isNetworkAvailable
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ResponseException
import io.ktor.client.features.feature
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.JsonSerializer
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.ResetContent
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.util.reflect.typeInfo
import io.ktor.util.toMap
import io.ktor.utils.io.errors.IOException

/** Create a wrapper which allow doing requests which will be transformed to `NetworkEither` */
public class NetworkEitherKtor(@PublishedApi internal val client: HttpClient) {

    public suspend inline operator fun <reified F, reified S> invoke(
        request: HttpClient.() -> HttpResponse
    ): NetworkEither<F, S> {
        val jsonFeature: JsonFeature =
            checkNotNull(client.feature(JsonFeature)) {
                "JsonFeature is missing and it is mandatory"
            }

        val jsonSerializer: JsonSerializer = jsonFeature.serializer
        return try {
            request(client).asNetworkEither(jsonSerializer)
        } catch (throwable: Throwable) {
            throwable.asNetworkEither(jsonSerializer)
        }
    }
}

@PublishedApi
internal suspend inline fun <reified F, reified S> HttpResponse.asNetworkEither(
    jsonSerializer: JsonSerializer,
): NetworkEither<F, S> =
    with(this) {
        if (content.availableForRead == 0) {
            val statusCode =
                if (status != NoContent || status != ResetContent) NoContent else status
            buildNetworkSuccess(Unit as S, HttpStatusCode(statusCode.value), headers.toMap())
        } else {
            buildNetworkSuccess(
                jsonSerializer.read(typeInfo<S>(), receive()) as S,
                HttpStatusCode(status.value),
                headers.toMap()
            )
        }
    }

@PublishedApi
internal suspend inline fun <reified F, reified S> ResponseException.serializeAsError(
    jsonSerializer: JsonSerializer,
): NetworkEither<F, S> =
    try {
        with(response) {
            buildNetworkFailureHttp(
                jsonSerializer.read(typeInfo<F>(), receive()) as F,
                HttpStatusCode(status.value),
                headers.toMap()
            )
        }
    } catch (throwable: Throwable) {
        buildNetworkFailureUnknown(throwable)
    }

@PublishedApi
internal suspend inline fun <reified F, reified S> Throwable.asNetworkEither(
    jsonSerializer: JsonSerializer,
): NetworkEither<F, S> =
    when (this) {
        is ResponseException -> serializeAsError(jsonSerializer)
        is UnresolvedAddressException -> localOrFailureRemote()
        is IOException -> localOrFailureRemote()
        else -> buildNetworkFailureUnknown(this)
    }

@PublishedApi
internal fun <F, S> localOrFailureRemote(): NetworkEither<F, S> =
    if (isNetworkAvailable) buildNetworkFailureRemote() else buildNetworkFailureLocal()
