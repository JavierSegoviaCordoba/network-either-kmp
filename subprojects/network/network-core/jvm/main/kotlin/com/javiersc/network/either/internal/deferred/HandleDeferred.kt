package com.javiersc.network.either.internal.deferred

import com.javiersc.network.either.Headers
import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import com.javiersc.network.either.internal.utils.printlnError
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
                deferred.complete(success(body, code, headers))
            } else handleNullBody(deferred, code, headers)
        }
        in 400..599 -> {
            if (errorBody != null) {
                deferred.complete(httpFailure(errorBody, code, headers))
            } else handleNullErrorBody(deferred, code, headers)
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun <F, S> handleNullBody(
    deferred: CompletableDeferred<NetworkEither<F, S>>,
    code: Int,
    headers: Headers,
) {
    try {
        deferred.complete(success(Unit as S, code, headers))
    } catch (e: ClassCastException) {
        printlnError(
            """
               | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # # #
               | # NetworkResponse should use Unit as Success type when body is null #
               | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
            """.trimMargin()
        )
        deferred.complete(unknownFailure(e))
    }
}

@Suppress("UNCHECKED_CAST")
private fun <F, S> handleNullErrorBody(
    deferred: CompletableDeferred<NetworkEither<F, S>>,
    code: Int,
    headers: Headers,
) {
    try {
        deferred.complete(httpFailure(Unit as F, code, headers))
    } catch (e: ClassCastException) {
        printlnError(
            """
               | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # #
               | # NetworkResponse should use Unit as Error type when body is null #
               | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
            """.trimMargin()
        )
        deferred.complete(unknownFailure(e))
    }
}
