package com.javiersc.network.either.internal.utils

import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import retrofit2.HttpException
import retrofit2.Response

internal val HttpException.httpStatusCode: HttpStatusCode
    get() = code() toHttpStatusCode message()

internal val Response<*>.httpStatusCode: HttpStatusCode
    get() = code() toHttpStatusCode message()

internal val HttpException.headers: Headers
    get() = headersOf(*response()?.headers().toOkHttpHeaders())

internal val Response<*>.headers: Headers
    get() = headersOf(*headers().toOkHttpHeaders())

private fun okhttp3.Headers?.toOkHttpHeaders(): Array<Pair<String, List<String>>> {
    return this?.toMultimap()?.map { header -> header.key to header.value }?.toTypedArray()
        ?: arrayOf("Content-Length" to listOf("0"))
}
