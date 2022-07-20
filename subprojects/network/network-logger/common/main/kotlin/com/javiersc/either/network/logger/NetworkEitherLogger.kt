@file:Suppress("TopLevelPropertyNaming")

package com.javiersc.either.network.logger

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.extensions.fold
import com.javiersc.mokoki.MokokiLogger
import com.javiersc.mokoki.Priority
import com.javiersc.mokoki.logE
import com.javiersc.mokoki.logI
import com.javiersc.mokoki.logWTF
import com.javiersc.mokoki.serialization.PrintSerializableMokokiLogger
import kotlinx.serialization.Serializable

public inline fun <reified F, reified S> NetworkEither<F, S>.alsoLog(
    tag: String = "NetworkEither"
): NetworkEither<F, S> {
    if (MokokiLogger.loggers.isEmpty()) {
        MokokiLogger.install(PrintSerializableMokokiLogger(Priority.VERBOSE))
    }

    fold(
        success = { data, code, headers -> logI(tag) { Response(data, code.value, headers) } },
        failureHttp = { error, code, headers ->
            logE(tag) { Response(error, code.value, headers) }
        },
        failureLocal = { logE(tag) { "Internet not available" } },
        failureRemote = { logE(tag) { "Remote not available" } },
        failureUnknown = { throwable -> logWTF(tag) { throwable.stackTraceToString() } },
    )
    return this
}

@Serializable
@PublishedApi
internal data class Response<T>(val body: T, val code: Int, val headers: Map<String, List<String>>)
