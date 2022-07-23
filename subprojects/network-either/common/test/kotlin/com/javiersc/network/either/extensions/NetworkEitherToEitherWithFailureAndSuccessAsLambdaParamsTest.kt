package com.javiersc.network.either.extensions

import arrow.core.Either
import com.javiersc.network.either.Headers
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.NetworkEither.Companion.localFailure
import com.javiersc.network.either.NetworkEither.Companion.remoteFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import com.javiersc.network.either.NetworkFailureHttp
import com.javiersc.network.either.NetworkSuccess
import io.kotest.matchers.shouldBe
import io.ktor.http.headersOf
import io.ktor.util.toMap
import kotlin.test.Test

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
        success(success, 200, headers)
            .toEither(
                httpFailure = NetworkFailureHttp<Int>::error,
                localFailure = { failureLocal },
                remoteFailure = { failureRemote },
                unknownFailure = { failureUnknown },
                success = NetworkSuccess<Int>::data,
            )
            .shouldBe(Either.Right(success))
    }

    @Test
    fun `Transform a NetworkEither to Either with failure http`() {
        httpFailure(failureHttp, 400, headers)
            .toEither(
                httpFailure = NetworkFailureHttp<Int>::error,
                localFailure = { failureLocal },
                remoteFailure = { failureRemote },
                unknownFailure = { failureUnknown },
                success = NetworkSuccess<Int>::data,
            )
            .shouldBe(Either.Left(failureHttp))
    }

    @Test
    fun `Transform a NetworkEither to Either with failure local`() {
        localFailure()
            .toEither(
                httpFailure = NetworkFailureHttp<Int>::error,
                localFailure = { failureLocal },
                remoteFailure = { failureRemote },
                unknownFailure = { failureUnknown },
                success = NetworkSuccess<Int>::data,
            )
            .shouldBe(Either.Left(failureLocal))
    }

    @Test
    fun `Transform a NetworkEither to Either with failure remote`() {
        remoteFailure()
            .toEither(
                httpFailure = NetworkFailureHttp<Int>::error,
                localFailure = { failureLocal },
                remoteFailure = { failureRemote },
                unknownFailure = { failureUnknown },
                success = NetworkSuccess<Int>::data,
            )
            .shouldBe(Either.Left(failureRemote))
    }

    @Test
    fun `Transform a NetworkEither to Either with failure unknown`() {
        unknownFailure(IllegalStateException())
            .toEither(
                httpFailure = NetworkFailureHttp<Int>::error,
                localFailure = { failureLocal },
                remoteFailure = { failureRemote },
                unknownFailure = { failureUnknown },
                success = NetworkSuccess<Int>::data,
            )
            .shouldBe(Either.Left(failureUnknown))
    }
}
