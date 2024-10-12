package com.javiersc.network.either.resource

import com.javiersc.network.either.Headers
import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkFailure
import com.javiersc.network.either.NetworkFailureHttp
import com.javiersc.network.either.NetworkFailureLocal
import com.javiersc.network.either.NetworkFailureRemote
import com.javiersc.network.either.NetworkFailureUnknown
import com.javiersc.network.either.NetworkSuccess
import com.javiersc.resource.either.ResourceEither.Companion.resourceFailure
import com.javiersc.resource.either.ResourceEither.Companion.resourceSuccess
import io.kotest.matchers.shouldBe
import io.ktor.http.headersOf
import io.ktor.util.toMap
import kotlin.test.Test

class NetworkEitherToResourceTests {

    private val success = 1
    private val failureHttp = 0
    private val failureLocal = 1000
    private val failureRemote = 2000
    private val failureUnknown = 3000
    private val headers: Headers
        get() = headersOf("A" to listOf("B")).toMap()

    @Test
    fun Transform_a_NetworkEither_to_Resource_with_success_input() {
        NetworkEither.success(success, 200, headers)
            .toResource(
                failure = { failure: NetworkFailure<Int> ->
                    when (failure) {
                        is NetworkFailureHttp<Int> -> resourceFailure(failure.error)
                        is NetworkFailureLocal -> resourceFailure(failureLocal)
                        is NetworkFailureRemote -> resourceFailure(failureRemote)
                        is NetworkFailureUnknown -> resourceFailure(failureUnknown)
                    }
                },
                success = { success: NetworkSuccess<Int> -> resourceSuccess(success.data) },
            )
            .shouldBe(resourceSuccess(success))

        NetworkEither.success(success, 200, headers)
            .toResource(
                httpError = NetworkEither.Failure.Http<Int>::error,
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
                success = NetworkSuccess<Int>::data,
                isLoading = false,
            )
            .shouldBe(resourceSuccess(success))
    }

    @Test
    fun Transform_a_NetworkEither_to_Resource_with_failure_http() {
        NetworkEither.buildHttpFailure(failureHttp, 400, headers)
            .toResource(
                failure = { failure: NetworkFailure<Int> ->
                    when (failure) {
                        is NetworkFailureHttp<Int> -> resourceFailure(failure.error)
                        is NetworkFailureLocal -> resourceFailure(failureLocal)
                        is NetworkFailureRemote -> resourceFailure(failureRemote)
                        is NetworkFailureUnknown -> resourceFailure(failureUnknown)
                    }
                },
                success = { success: NetworkSuccess<Int> -> resourceSuccess(success.data) },
            )
            .shouldBe(resourceFailure(failureHttp))

        NetworkEither.buildHttpFailure(failureHttp, 200, headers)
            .toResource(
                httpError = NetworkFailureHttp<Int>::error,
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
                success = NetworkSuccess<Int>::data,
                isLoading = false,
            )
            .shouldBe(resourceFailure(failureHttp))
    }

    @Test
    fun Transform_a_NetworkEither_to_Resource_with_failure_local() {
        NetworkEither.localFailure()
            .toResource(
                failure = { failure: NetworkFailure<Int> ->
                    when (failure) {
                        is NetworkFailureHttp<Int> -> resourceFailure(failure.error)
                        is NetworkFailureLocal -> resourceFailure(failureLocal)
                        is NetworkFailureRemote -> resourceFailure(failureRemote)
                        is NetworkFailureUnknown -> resourceFailure(failureUnknown)
                    }
                },
                success = { success: NetworkSuccess<Int> -> resourceSuccess(success.data) },
            )
            .shouldBe(resourceFailure(failureLocal))

        NetworkEither.localFailure()
            .toResource(
                httpError = NetworkFailureHttp<Int>::error,
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
                success = NetworkSuccess<Int>::data,
                isLoading = false,
            )
            .shouldBe(resourceFailure(failureLocal))
    }

    @Test
    fun Transform_a_NetworkEither_to_Either_with_failure_remote() {
        NetworkEither.remoteFailure()
            .toResource(
                failure = { failure: NetworkFailure<Int> ->
                    when (failure) {
                        is NetworkFailureHttp<Int> -> resourceFailure(failure.error)
                        is NetworkFailureLocal -> resourceFailure(failureLocal)
                        is NetworkFailureRemote -> resourceFailure(failureRemote)
                        is NetworkFailureUnknown -> resourceFailure(failureUnknown)
                    }
                },
                success = { success: NetworkSuccess<Int> -> resourceSuccess(success.data) },
            )
            .shouldBe(resourceFailure(failureRemote))

        NetworkEither.remoteFailure()
            .toResource(
                httpError = NetworkFailureHttp<Int>::error,
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
                success = NetworkSuccess<Int>::data,
                isLoading = false,
            )
            .shouldBe(resourceFailure(failureRemote))
    }

    @Test
    fun Transform_a_NetworkEither_to_Either_with_failure_unknown() {
        NetworkEither.unknownFailure(IllegalStateException())
            .toResource(
                failure = { failure: NetworkFailure<Int> ->
                    when (failure) {
                        is NetworkFailureHttp<Int> -> resourceFailure(failure.error)
                        is NetworkFailureLocal -> resourceFailure(failureLocal)
                        is NetworkFailureRemote -> resourceFailure(failureRemote)
                        is NetworkFailureUnknown -> resourceFailure(failureUnknown)
                    }
                },
                success = { success: NetworkSuccess<Int> -> resourceSuccess(success.data) },
            )
            .shouldBe(resourceFailure(failureUnknown))

        NetworkEither.unknownFailure(IllegalStateException())
            .toResource(
                httpError = NetworkFailureHttp<Int>::error,
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
                success = NetworkSuccess<Int>::data,
                isLoading = false,
            )
            .shouldBe(resourceFailure(failureUnknown))
    }

    @Test
    fun Transform_a_NetworkEither_to_Either_with_failure_unknown_and_loading() {
        NetworkEither.unknownFailure(IllegalStateException())
            .toResource(
                httpError = NetworkFailureHttp<Int>::error,
                localError = { failureLocal },
                remoteError = { failureRemote },
                unknownError = { failureUnknown },
                success = NetworkSuccess<Int>::data,
                isLoading = true,
            )
            .shouldBe(resourceFailure(failureUnknown, isLoading = true))
    }
}
