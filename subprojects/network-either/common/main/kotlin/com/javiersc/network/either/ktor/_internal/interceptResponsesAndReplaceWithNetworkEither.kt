package com.javiersc.network.either.ktor._internal

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.toMap

internal fun interceptSuccessesAndReplaceWithNetworkEitherSuccess(client: HttpClient) {
    client.responsePipeline.intercept(HttpResponsePipeline.After) {
        if (isNetworkEither) return@intercept
        val code: Int = context.response.status.value
        val headers: Map<String, List<String>> = context.response.headers.toMap()
        val response = subject.response
        when (code) {
            in SUCCESS_RANGE -> {
                val networkEither: NetworkEither<Nothing, *> =
                    success(data = response, code = code, headers = headers.toMap())
                proceedWith(HttpResponseContainer(subject.expectedType, networkEither))
            }
            else -> {
                val networkEither: NetworkEither<*, Nothing> =
                    httpFailure(error = response, code = code, headers = headers.toMap())
                proceedWith(HttpResponseContainer(subject.expectedType, networkEither))
            }
        }
    }
}
