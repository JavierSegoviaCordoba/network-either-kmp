package com.javiersc.either.network.ktor._internal

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
import io.ktor.util.toMap
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.Job

internal fun HttpClient.fakeFailureHttpClientCall(
    outgoingContent: OutgoingContent
): HttpClientCall {
    val responseData: HttpResponseData = failureHttpResponseData(outgoingContent)
    val call: HttpClientCall =
        object : HttpClientCall(this@fakeFailureHttpClientCall) {
            val httpRequestData: HttpRequestData =
                HttpRequestBuilder()
                    .apply {
                        setBody(outgoingContent)
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

private fun failureHttpResponseData(outgoingContent: OutgoingContent): HttpResponseData {
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
        callContext = Job(),
    )
}
