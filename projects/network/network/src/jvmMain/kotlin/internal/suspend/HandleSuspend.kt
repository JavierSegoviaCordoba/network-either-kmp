package com.javiersc.either.network.internal.suspend

import com.javiersc.either.network.Headers
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.internal.utils.printlnError
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
) = with(callback) {
    @Suppress("MagicNumber")
    when (code) {
        in 200..299 -> {
            if (body != null) onResponse(call, Response.success(buildNetworkSuccess(body, code, headers)))
            else handleNullBody(callback, call, code, headers)
        }
        in 400..599 -> {
            if (errorBody != null) onResponse(
                call,
                Response.success(buildNetworkFailureHttp(errorBody, code, headers))
            )
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
        callback.onResponse(call, Response.success(buildNetworkSuccess(Unit as S, code, headers)))
    } catch (e: ClassCastException) {
        printlnError(
            """
               | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # # #
               | # NetworkResponse should use Unit as Success type when body is null #
               | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
            """.trimMargin()
        )
        callback.onResponse(call, Response.success(buildNetworkFailureUnknown(e)))
    }
}

@Suppress("UNCHECKED_CAST")
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
    callback.onResponse(
        call,
        Response.success(
            buildNetworkFailureUnknown(
                ClassCastException("NetworkResponse should use Unit as Error type when body is null")
            )
        )
    )
}
