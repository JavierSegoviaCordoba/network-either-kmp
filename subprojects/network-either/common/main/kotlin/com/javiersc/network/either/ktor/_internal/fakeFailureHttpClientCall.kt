package com.javiersc.network.either.ktor._internal

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.request.DefaultHttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.setBody
import io.ktor.client.statement.DefaultHttpResponse
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.content.OutgoingContent
import io.ktor.util.AttributeKey
import io.ktor.util.date.GMTDate
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.toMap
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray

@OptIn(InternalAPI::class)
internal fun PipelineContext<Any, HttpRequestBuilder>.fakeHttpFailureClientCall(
    client: HttpClient
): HttpClientCall {
    val responseData: HttpResponseData = failureHttpResponseData(RemoteErrorOutgoing)
    val call: HttpClientCall =
        object : HttpClientCall(client) {
            val httpRequestData: HttpRequestData =
                HttpRequestBuilder()
                    .apply {
                        setBody(RemoteErrorOutgoing)
                        attributes.put(AttributeKey("ExpectSuccessAttributeKey"), false)
                    }
                    .build()

            init {
                request = DefaultHttpRequest(this, httpRequestData)
                response = DefaultHttpResponse(this, responseData)
            }
        }

    return call
}

@OptIn(InternalAPI::class)
internal fun PipelineContext<Any, HttpRequestBuilder>.fakeLocalFailureClientCall(
    client: HttpClient
): HttpClientCall {
    val call: HttpClientCall =
        object : HttpClientCall(client) {
            val httpRequestData: HttpRequestData =
                HttpRequestBuilder()
                    .apply {
                        setBody(LocalErrorOutgoing)
                        attributes.put(AttributeKey("ExpectSuccessAttributeKey"), false)
                    }
                    .build()
            val responseData: HttpResponseData = failureHttpResponseData(LocalErrorOutgoing)

            init {
                request = DefaultHttpRequest(this, httpRequestData)
                response = DefaultHttpResponse(this, responseData)
            }
        }

    return call
}

internal fun PipelineContext<Any, HttpRequestBuilder>.failureHttpResponseData(
    outgoingContent: OutgoingContent
): HttpResponseData {
    val status = outgoingContent.status
    checkNotNull(status) { "Status must not be null and this should not be happening" }
    val headers = outgoingContent.headers
    check(headers.toMap().keys.contains("NetworkEither")) {
        "Headers must contain `NetworkEither` key and this should not be happening"
    }
    return HttpResponseData(
        statusCode = status,
        requestTime = GMTDate(),
        headers = outgoingContent.headers,
        version = HttpProtocolVersion.HTTP_1_1,
        body = ByteReadChannel("".toByteArray(Charsets.UTF_8)),
        callContext = context.executionContext,
    )
}
