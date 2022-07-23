package com.javiersc.network.either.internal.suspend

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.internal.utils.headers
import com.javiersc.network.either.internal.utils.httpStatusCode
import io.ktor.util.toMap
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.HttpException

internal fun <F : Any, S : Any> HttpException.httpExceptionSuspendHandler(
    errorConverter: Converter<ResponseBody, F>,
    call: NetworkEitherSuspendCall<F, S>,
    callback: Callback<NetworkEither<F, S>>,
) {
    val errorBody: F? = response()?.errorBody()?.let(errorConverter::convert)

    handleSuspend(call, callback, httpStatusCode.value, null, errorBody, headers = headers.toMap())
}
