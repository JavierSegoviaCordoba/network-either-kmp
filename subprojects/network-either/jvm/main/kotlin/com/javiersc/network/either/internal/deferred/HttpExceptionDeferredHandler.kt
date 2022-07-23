package com.javiersc.network.either.internal.deferred

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.internal.utils.headers
import com.javiersc.network.either.internal.utils.httpStatusCode
import io.ktor.util.toMap
import kotlinx.coroutines.CompletableDeferred
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException

internal fun <F : Any, S : Any> HttpException.httpExceptionDeferredHandler(
    errorConverter: Converter<ResponseBody, F>,
    deferred: CompletableDeferred<NetworkEither<F, S>>
) {
    val errorBody: F? = response()?.errorBody()?.let(errorConverter::convert)
    handleDeferred(deferred, httpStatusCode.value, body = null, errorBody, headers.toMap())
}
