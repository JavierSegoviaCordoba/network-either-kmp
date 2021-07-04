package com.javiersc.either.network.resource

import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.Success
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
import kotlin.test.Test

internal class NetworkEitherToResourceWithFailureAndSuccessAsLambdaParamsTest {

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
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> buildResourceFailure(failure.error)
                        is Failure.Local -> buildResourceFailure(failureLocal)
                        is Failure.Remote -> buildResourceFailure(failureRemote)
                        is Failure.Unknown -> buildResourceFailure(failureUnknown)
                    }
                },
                right = { success: Success<Int> -> buildResourceSuccess(success.data) },
            ) shouldBe buildResourceSuccess(success)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure http`() {
        buildNetworkFailureHttp<Int, Int>(failureHttp, HttpStatusCode(400), headers)
            .toResource(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> buildResourceFailure(failure.error)
                        is Failure.Local -> buildResourceFailure(failureLocal)
                        is Failure.Remote -> buildResourceFailure(failureRemote)
                        is Failure.Unknown -> buildResourceFailure(failureUnknown)
                    }
                },
                right = { success: Success<Int> -> buildResourceSuccess(success.data) },
            ) shouldBe buildResourceFailure(failureHttp)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure local`() {
        buildNetworkFailureLocal<Int, Int>()
            .toResource(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> buildResourceFailure(failure.error)
                        is Failure.Local -> buildResourceFailure(failureLocal)
                        is Failure.Remote -> buildResourceFailure(failureRemote)
                        is Failure.Unknown -> buildResourceFailure(failureUnknown)
                    }
                },
                right = { success: Success<Int> -> buildResourceSuccess(success.data) },
            ) shouldBe buildResourceFailure(failureLocal)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure remote`() {
        buildNetworkFailureRemote<Int, Int>()
            .toResource(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> buildResourceFailure(failure.error)
                        is Failure.Local -> buildResourceFailure(failureLocal)
                        is Failure.Remote -> buildResourceFailure(failureRemote)
                        is Failure.Unknown -> buildResourceFailure(failureUnknown)
                    }
                },
                right = { success: Success<Int> -> buildResourceSuccess(success.data) },
            ) shouldBe buildResourceFailure(failureRemote)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure unknown`() {
        buildNetworkFailureUnknown<Int, Int>(IllegalStateException())
            .toResource(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> buildResourceFailure(failure.error)
                        is Failure.Local -> buildResourceFailure(failureLocal)
                        is Failure.Remote -> buildResourceFailure(failureRemote)
                        is Failure.Unknown -> buildResourceFailure(failureUnknown)
                    }
                },
                right = { success: Success<Int> -> buildResourceSuccess(success.data) },
            ) shouldBe buildResourceFailure(failureUnknown)
    }
}
