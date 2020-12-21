package com.javiersc.either.network.logger

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.extensions.fold
import com.javiersc.logger.extensions.logE
import com.javiersc.logger.serialization.LoggerSerialization
import com.javiersc.logger.serialization.extensions.logSerializableE
import com.javiersc.logger.serialization.extensions.logSerializableS
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

public fun <F, S> NetworkEither<F, S>.alsoPrettyPrint(
    tag: String? = null,
    failureSerializer: KSerializer<F>,
    successSerializer: KSerializer<S>,
): NetworkEither<F, S> {
    fold(
        success = { data, code, headers ->
            logSerializableS(tag, Response.serializer(successSerializer), Response(data, code, headers))
        },
        http = { error, code, headers ->
            logSerializableE(tag, Response.serializer(failureSerializer), Response(error, code, headers))
        },
        local = { if (tag != null) logE(tag, LocalError) else logE(LocalError) },
        remote = { if (tag != null) logE(tag, RemoteError) else logE(RemoteError) },
        unknown = { throwable ->
            with(throwable.stackTraceToString()) {
                if (tag != null) logE(tag, this) else logE(this)
            }
        },
    )
    return this
}

public fun <F, S> NetworkEither<F, S>.alsoPrettyPrint(
    tag: String? = null,
    logger: LoggerSerialization,
    failureSerializer: KSerializer<F>,
    successSerializer: KSerializer<S>,
): NetworkEither<F, S> {
    with(logger) {
        fold(
            success = { data, code, headers ->
                serializableS(tag, Response.serializer(successSerializer), Response(data, code, headers))
            },
            http = { error, code, headers ->
                serializableD(tag, Response.serializer(failureSerializer), Response(error, code, headers))
            },
            local = { e(tag, LocalError) },
            remote = { e(tag, RemoteError) },
            unknown = { throwable -> e(tag, throwable.stackTraceToString()) },
        )
    }
    return this
}

public inline fun <reified F, reified S> NetworkEither<F, S>.alsoPrettyPrint(
    tag: String? = null,
): NetworkEither<F, S> = alsoPrettyPrint(tag, serializer(), serializer())

public inline fun <reified F, reified S> NetworkEither<F, S>.alsoPrettyPrint(
    tag: String? = null,
    logger: LoggerSerialization,
): NetworkEither<F, S> = alsoPrettyPrint(tag, logger, serializer(), serializer())

@Serializable
internal data class Response<T>(val body: T, val code: Int, val headers: Map<String, List<String>>)

private const val LocalError = "Internet not available"
private const val RemoteError = "Remote not available"
