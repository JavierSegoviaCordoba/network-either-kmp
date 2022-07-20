package com.javiersc.either.network.ktor._internal

import com.javiersc.either.network.HttpStatusCode as NetworkEitherHttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkSuccess
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.toMap

internal fun interceptSuccessesAndReplaceWithNetworkEitherSuccess(scope: HttpClient) {
    scope.responsePipeline.intercept(HttpResponsePipeline.After) {
        if (isEither || isNetworkEither) return@intercept
        val statusCode: Int = context.response.status.value
        val headers: Map<String, List<String>> = context.response.headers.toMap()
        val response = subject.response
        val code = NetworkEitherHttpStatusCode(statusCode)
        when (statusCode) {
            in 200..299 -> {
                val networkEither: NetworkEither<Nothing, *> =
                    buildNetworkSuccess(data = response, code = code, headers = headers.toMap())
                proceedWith(HttpResponseContainer(subject.expectedType, networkEither))
            }
            else -> {
                val networkEither: NetworkEither<*, Nothing> =
                    buildNetworkFailureHttp(
                        error = response,
                        code = code,
                        headers = headers.toMap()
                    )
                proceedWith(HttpResponseContainer(subject.expectedType, networkEither))
            }
        }
    }
}
