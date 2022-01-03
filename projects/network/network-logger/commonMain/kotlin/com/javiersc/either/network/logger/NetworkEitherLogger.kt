@file:Suppress("TopLevelPropertyNaming")

package com.javiersc.either.network.logger

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.extensions.fold
import com.javiersc.mokoki.extensions.logE
import com.javiersc.mokoki.serialization.extensions.logSerializableE
import com.javiersc.mokoki.serialization.extensions.logSerializableV
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

public fun <F, S> NetworkEither<F, S>.alsoPrettyPrint(
    tag: String = "NetworkEither",
    failureSerializer: KSerializer<F>,
    successSerializer: KSerializer<S>,
): NetworkEither<F, S> {
    fold(
        success = { data, code, headers ->
            logSerializableV(
                tag,
                Response.serializer(successSerializer),
                Response(data, code.value, headers)
            )
        },
        failureHttp = { error, code, headers ->
            logSerializableE(
                tag,
                Response.serializer(failureSerializer),
                Response(error, code.value, headers)
            )
        },
        failureLocal = { logE(tag, LocalError) },
        failureRemote = { logE(tag, RemoteError) },
        failureUnknown = { throwable -> logE(tag, throwable.stackTraceToString()) },
    )
    return this
}

public inline fun <reified F, reified S> NetworkEither<F, S>.alsoPrettyPrint(
    tag: String = "NetworkEither",
): NetworkEither<F, S> = alsoPrettyPrint(tag, serializer(), serializer())

@Serializable
internal data class Response<T>(val body: T, val code: Int, val headers: Map<String, List<String>>)

private const val LocalError = "Internet not available"
private const val RemoteError = "Remote not available"
