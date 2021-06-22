package com.javiersc.either.network.extensions

import com.javiersc.either.Either
import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.Success
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.buildNetworkFailureRemote
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import io.kotest.matchers.shouldBe
import io.ktor.http.headersOf
import io.ktor.util.toMap
import org.junit.Test

internal class NetworkEitherToEitherWithFailureAndSuccessAsLambdaParamsTest {

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
            .toEither(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> failure.error
                        is Failure.Local -> failureLocal
                        is Failure.Remote -> failureRemote
                        is Failure.Unknown -> failureUnknown
                    }
                },
                right = { success: Success<Int> -> success.data },
            ) shouldBe Either.Right(success)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure http`() {
        buildNetworkFailureHttp<Int, Int>(failureHttp, HttpStatusCode(400), headers)
            .toEither(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> failure.error
                        is Failure.Local -> failureLocal
                        is Failure.Remote -> failureRemote
                        is Failure.Unknown -> failureUnknown
                    }
                },
                right = { success: Success<Int> -> success.data },
            ) shouldBe Either.Left(failureHttp)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure local`() {
        buildNetworkFailureLocal<Int, Int>()
            .toEither(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> failure.error
                        is Failure.Local -> failureLocal
                        is Failure.Remote -> failureRemote
                        is Failure.Unknown -> failureUnknown
                    }
                },
                right = { success: Success<Int> -> success.data },
            ) shouldBe Either.Left(failureLocal)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure remote`() {
        buildNetworkFailureRemote<Int, Int>()
            .toEither(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> failure.error
                        is Failure.Local -> failureLocal
                        is Failure.Remote -> failureRemote
                        is Failure.Unknown -> failureUnknown
                    }
                },
                right = { success: Success<Int> -> success.data },
            ) shouldBe Either.Left(failureRemote)
    }

    @Test
    fun `Transform a NetworkEither to Either with failure unknown`() {
        buildNetworkFailureUnknown<Int, Int>(IllegalStateException())
            .toEither(
                left = { failure: Failure<Int> ->
                    when (failure) {
                        is Failure.Http<Int> -> failure.error
                        is Failure.Local -> failureLocal
                        is Failure.Remote -> failureRemote
                        is Failure.Unknown -> failureUnknown
                    }
                },
                right = { success: Success<Int> -> Either.Right(success.data) },
            ) shouldBe Either.Left(failureUnknown)
    }
}
