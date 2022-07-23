package com.javiersc.network.either.internal.suspend

import com.javiersc.network.either.Headers
import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import com.javiersc.network.either.internal.utils.printlnError
import retrofit2.Callback
import retrofit2.Response

@Suppress("LongParameterList")
internal fun <F : Any, S : Any> handleSuspend(
    call: NetworkEitherSuspendCall<F, S>,
    callback: Callback<NetworkEither<F, S>>,
    code: Int,
    body: S?,
    errorBody: F?,
    headers: Headers,
) =
    with(callback) {
        @Suppress("MagicNumber")
        when (code) {
            in 200..299 -> {
                if (body != null) onResponse(call, Response.success(success(body, code, headers)))
                else handleNullBody(callback, call, code, headers)
            }
            in 400..599 -> {
                if (errorBody != null)
                    onResponse(call, Response.success(httpFailure(errorBody, code, headers)))
                else handleNullErrorBody(callback, call)
            }
        }
    }

@Suppress("UNCHECKED_CAST")
private fun <F : Any, S : Any> handleNullBody(
    callback: Callback<NetworkEither<F, S>>,
    call: NetworkEitherSuspendCall<F, S>,
    code: Int,
    headers: Headers,
) {
    try {
        callback.onResponse(call, Response.success(success(Unit as S, code, headers)))
    } catch (e: ClassCastException) {
        printlnError(
            """
               | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # # #
               | # NetworkResponse should use Unit as Success type when body is null #
               | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
            """.trimMargin()
        )
        callback.onResponse(call, Response.success(unknownFailure(e)))
    }
}

private fun <F : Any, S : Any> handleNullErrorBody(
    callback: Callback<NetworkEither<F, S>>,
    call: NetworkEitherSuspendCall<F, S>,
) {
    printlnError(
        """
           | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # # 
           | # NetworkResponse should use Unit as Error type when body is null # 
           | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
        """.trimMargin()
    )
    val classCastException =
        ClassCastException("NetworkResponse should use Unit as Error type when body is null")
    callback.onResponse(call, Response.success(unknownFailure(classCastException)))
}
