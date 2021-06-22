@file:Suppress("LongParameterList")

package com.javiersc.either.network.resource

import com.javiersc.either.Either
import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.Success
import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.buildResourceFailure
import com.javiersc.either.resource.buildResourceSuccess
import kotlin.jvm.JvmName

/**
 * Map a `NetworkEither` to `ResourceEither` with the network `Failure` and `Success` as lambda
 * params
 *
 * ```
 * networkEither.map(
 *     left = { failure: Failure<SomeFailure> -> buildResource(failure) },
 *     right = { success: Success<SomeSuccess> -> buildResource(failure) },
 * )
 * ```
 */
public fun <NF, NS, F, S> NetworkEither<NF, NS>.toResource(
    left: (Failure<NF>) -> ResourceEither<F, S>,
    right: (Success<NS>) -> ResourceEither<F, S>,
): ResourceEither<F, S> =
    when (this) {
        is Either.Left -> left(this.left)
        is Either.Right -> right(this.right)
    }

/**
 * Map a `NetworkEither` to `ResourceEither` with network `Failure` and `Success` unwrapped as
 * lambda params
 *
 * ```
 * networkEither.map(
 *     success = {
 *         data: SomeSuccess, code: HttpStatusCode, headers: Headers -> buildResource(data, code, headers)
 *     },
 *     httpError = {
 *         error: SomeFailure, code: HttpStatusCode, headers: Headers -> buildResource(error, code, headers)
 *     },
 *     localError = { buildResource() },
 *     remoteError = { buildResource() }
 *     unknownError = { unknownError: Throwable -> buildResource(unknownError) }
 * )
 * ```
 */
public fun <NF, NS, F, S> NetworkEither<NF, NS>.toResource(
    success: (NS, HttpStatusCode, Headers) -> S,
    httpError: (NF, HttpStatusCode, Headers) -> F,
    localError: () -> F,
    remoteError: () -> F,
    unknownError: (Throwable) -> F,
    isLoading: Boolean = false
): ResourceEither<F, S> =
    when (this) {
        is Either.Left ->
            when (val failure = this.left) {
                is Failure.Http ->
                    with(failure) {
                        buildResourceFailure(
                            failure = httpError(error, code, headers),
                            isLoading = isLoading
                        )
                    }
                is Failure.Local ->
                    buildResourceFailure(failure = localError(), isLoading = isLoading)
                is Failure.Remote ->
                    buildResourceFailure(failure = remoteError(), isLoading = isLoading)
                is Failure.Unknown ->
                    buildResourceFailure(
                        failure = unknownError(failure.throwable),
                        isLoading = isLoading
                    )
            }
        is Either.Right ->
            buildResourceSuccess(
                data = success(this.right.data, this.right.code, this.right.headers),
                isLoading = isLoading,
            )
    }

/**
 * Map a `NetworkEither` to `ResourceEither` with network `Failure` data and the `Success` data as
 * lambda params
 *
 * ```
 * networkEither.map(
 *     success = { data: SomeSuccess -> buildResource(data) },
 *     httpError = { error: SomeFailure -> buildResource(error) },
 *     localError = { buildResource() },
 *     remoteError = { buildResource() }
 *     unknownError = { unknownError: Throwable -> buildResource(unknownError) }
 * )
 * ```
 */
public fun <NF, NS, F, S> NetworkEither<NF, NS>.toResource(
    success: (NS) -> S,
    httpError: (NF) -> F,
    localError: () -> F,
    remoteError: () -> F,
    unknownError: (Throwable) -> F,
    isLoading: Boolean = false
): ResourceEither<F, S> =
    when (this) {
        is Either.Left ->
            when (val failure = this.left) {
                is Failure.Http ->
                    with(failure) {
                        buildResourceFailure(failure = httpError(error), isLoading = isLoading)
                    }
                is Failure.Local ->
                    buildResourceFailure(failure = localError(), isLoading = isLoading)
                is Failure.Remote ->
                    buildResourceFailure(failure = remoteError(), isLoading = isLoading)
                is Failure.Unknown ->
                    buildResourceFailure(
                        failure = unknownError(failure.throwable),
                        isLoading = isLoading
                    )
            }
        is Either.Right ->
            buildResourceSuccess(data = success(this.right.data), isLoading = isLoading)
    }

/**
 * Map a `NetworkEither` to `ResourceEither` with network `Failure` error code and the `Success`
 * data as lambda params
 *
 * ```
 * networkEither.map(
 *     success = { data: SomeSuccess -> buildResource(data) },
 *     httpError = { errorCode: HttpStatusCode -> buildResource(error) },
 *     localError = { buildResource() },
 *     remoteError = { buildResource() }
 *     unknownError = { unknownError: Throwable -> buildResource(unknownError) }
 * )
 * ```
 */
@JvmName("toResourceWithSuccessDataAndErrorCode")
public fun <NF, NS, F, S> NetworkEither<NF, NS>.toResource(
    success: (NS) -> S,
    httpError: (HttpStatusCode) -> F,
    localError: () -> F,
    remoteError: () -> F,
    unknownError: (Throwable) -> F,
    isLoading: Boolean = false
): ResourceEither<F, S> =
    when (this) {
        is Either.Left ->
            when (val failure = this.left) {
                is Failure.Http ->
                    with(failure) {
                        buildResourceFailure(failure = httpError(code), isLoading = isLoading)
                    }
                is Failure.Local ->
                    buildResourceFailure(failure = localError(), isLoading = isLoading)
                is Failure.Remote ->
                    buildResourceFailure(failure = remoteError(), isLoading = isLoading)
                is Failure.Unknown ->
                    buildResourceFailure(
                        failure = unknownError(failure.throwable),
                        isLoading = isLoading
                    )
            }
        is Either.Right ->
            buildResourceSuccess(data = success(this.right.data), isLoading = isLoading)
    }
