package com.javiersc.network.either

import arrow.core.Either
import io.kotest.matchers.shouldBe
import io.ktor.http.headersOf
import io.ktor.util.toMap
import kotlin.test.Test

internal class NetworkEitherToEither {

    private val success = 1
    private val failureHttp = 0
    private val failureLocal = 1000
    private val failureRemote = 2000
    private val failureUnknown = 3000
    private val headers: Headers
        get() = headersOf("A" to listOf("B")).toMap()

    @Test
    fun `Transform a NetworkEither to Either with success input`() {
        NetworkEither.success(success, 200, headers)
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
        NetworkEither.httpFailure(failureHttp, 400, headers)
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
        NetworkEither.localFailure()
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
        NetworkEither.remoteFailure()
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
        NetworkEither.unknownFailure(IllegalStateException())
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
