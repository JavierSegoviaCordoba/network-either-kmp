package com.javiersc.network.either.logger

import com.javiersc.mokoki.MokokiLogger
import com.javiersc.mokoki.Priority
import com.javiersc.mokoki.logE
import com.javiersc.mokoki.logI
import com.javiersc.mokoki.logWTF
import com.javiersc.mokoki.serialization.PrintSerializableMokokiLogger
import com.javiersc.network.either.NetworkEither
import kotlinx.serialization.Serializable

public inline fun <reified F, reified S> NetworkEither<F, S>.alsoLog(
    tag: String? = null
): NetworkEither<F, S> {
    if (MokokiLogger.loggers.isEmpty()) {
        MokokiLogger.install(PrintSerializableMokokiLogger(Priority.VERBOSE))
    }

    fold(
        httpFailure = { (error, code, headers) -> logI(tag) { Response(error, code, headers) } },
        localFailure = { logE(tag) { "Internet not available" } },
        remoteFailure = { logE(tag) { "Remote not available" } },
        unknownFailure = { throwable -> logWTF(tag) { throwable.stackTraceToString() } },
        success = { (data, code, headers) -> logI(tag) { Response(data, code, headers) } },
    )

    return this
}

@Serializable
@PublishedApi
internal data class Response<T>(val body: T, val code: Int, val headers: Map<String, List<String>>)
