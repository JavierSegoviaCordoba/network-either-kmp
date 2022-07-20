package com.javiersc.either.network.internal.deferred

import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.internal.utils.printlnError
import kotlinx.coroutines.CompletableDeferred

internal fun <F, S> handleDeferred(
    deferred: CompletableDeferred<NetworkEither<F, S>>,
    code: Int,
    body: S?,
    errorBody: F?,
    headers: Headers,
) {
    @Suppress("MagicNumber")
    when (code) {
        in 200..299 -> {
            if (body != null) {
                deferred.complete(buildNetworkSuccess(body, HttpStatusCode(code), headers))
            } else handleNullBody(deferred, code, headers)
        }
        in 400..599 -> {
            if (errorBody != null) {
                deferred.complete(buildNetworkFailureHttp(errorBody, HttpStatusCode(code), headers))
            } else handleNullErrorBody(deferred, code, headers)
        }
    }
}

private fun <F, S> handleNullBody(
    deferred: CompletableDeferred<NetworkEither<F, S>>,
    code: Int,
    headers: Headers,
) {
    try {
        @Suppress("UNCHECKED_CAST")
        deferred.complete(buildNetworkSuccess(Unit as S, HttpStatusCode(code), headers))
    } catch (e: ClassCastException) {
        printlnError(
            """
               | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # # #
               | # NetworkResponse should use Unit as Success type when body is null #
               | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
            """.trimMargin()
        )
        deferred.complete(buildNetworkFailureUnknown(e))
    }
}

private fun <F, S> handleNullErrorBody(
    deferred: CompletableDeferred<NetworkEither<F, S>>,
    code: Int,
    headers: Headers,
) {
    try {
        @Suppress("UNCHECKED_CAST")
        deferred.complete(buildNetworkFailureHttp(Unit as F, HttpStatusCode(code), headers))
    } catch (e: ClassCastException) {
        printlnError(
            """
               | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # #
               | # NetworkResponse should use Unit as Error type when body is null #
               | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
            """.trimMargin()
        )
        deferred.complete(buildNetworkFailureUnknown(e))
    }
}
