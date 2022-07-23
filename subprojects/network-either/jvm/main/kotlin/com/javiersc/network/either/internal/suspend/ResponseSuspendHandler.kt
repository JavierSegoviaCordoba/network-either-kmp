package com.javiersc.network.either.internal.suspend

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.internal.utils.headers
import com.javiersc.network.either.internal.utils.httpStatusCode
import com.javiersc.network.either.internal.utils.printlnError
import io.ktor.util.toMap
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

internal fun <F : Any, S : Any> Response<S>.responseSuspendHandler(
    errorConverter: Converter<ResponseBody, F>,
    call: NetworkEitherSuspendCall<F, S>,
    callback: Callback<NetworkEither<F, S>>,
) {
    val errorBody: F? =
        if (errorBody()?.contentLength() == 0L) null
        else
            runCatching { errorBody()?.let { errorConverter.convert(it) } }
                .getOrElse {
                    printlnError("Error body can't be serialized with the error object provided")
                        .run { null }
                }

    handleSuspend(call, callback, httpStatusCode.value, body(), errorBody, headers.toMap())
}
