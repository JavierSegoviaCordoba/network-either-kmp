package com.javiersc.either.network.resource

import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.buildNetworkFailureRemote
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.resource.buildResourceFailure
import com.javiersc.either.resource.buildResourceSuccess
import io.kotest.matchers.shouldBe
import io.ktor.http.headersOf
import io.ktor.util.toMap
import org.junit.Test

internal class NetworkEitherToResourceWithFailureDataAndSuccessDataAsLambdaParamsTest {

    private val success = 1
    private val failureHttp = 0
    private val failureLocal = 1000
    private val failureRemote = 2000
    private val failureUnknown = 3000
    private val headers: Headers
        get() = headersOf("A" to listOf("B")).toMap()

    @Test
    fun `Transform a NetworkEither to Either with success input`() {
        buildNetworkSuccess<Int, Int>(success, HttpStatusCode(200), headers)
            .toResource(
                success = { data: Int, _, _ -> data },
                httpError = { error: Int, _, _ -> error },
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
            ) shouldBe buildResourceSuccess(success)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure http`() {
        buildNetworkFailureHttp<Int, Int>(failureHttp, HttpStatusCode(200), headers)
            .toResource(
                success = { data: Int -> data },
                httpError = { error: Int -> error },
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
            ) shouldBe buildResourceFailure(failureHttp)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure local`() {
        buildNetworkFailureLocal<Int, Int>()
            .toResource(
                success = { data: Int, _, _ -> data },
                httpError = { error: Int, _, _ -> error },
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
            ) shouldBe buildResourceFailure(failureLocal)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure remote`() {
        buildNetworkFailureRemote<Int, Int>()
            .toResource(
                success = { data: Int, _, _ -> data },
                httpError = { error: Int, _, _ -> error },
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
            ) shouldBe buildResourceFailure(failureRemote)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure unknown`() {
        buildNetworkFailureUnknown<Int, Int>(IllegalStateException())
            .toResource(
                success = { data: Int, _, _ -> data },
                httpError = { error: Int, _, _ -> error },
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
            ) shouldBe buildResourceFailure(failureUnknown)
    }
}
