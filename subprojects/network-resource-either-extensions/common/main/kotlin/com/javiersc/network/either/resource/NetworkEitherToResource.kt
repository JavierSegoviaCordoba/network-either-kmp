@file:Suppress("LongParameterList")

package com.javiersc.network.either.resource

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Failure
import com.javiersc.resource.either.ResourceEither
import com.javiersc.resource.either.ResourceEither.Companion.resourceFailure
import com.javiersc.resource.either.ResourceEither.Companion.resourceSuccess

/**
 * Map a `NetworkEither` to `ResourceEither` with the network `Failure` and `Success` as lambda
 * params
 *
 * ```
 * networkEither.toResource(
 *     left = { failure: Failure<SomeFailure> ->
 *         buildResource(failure)
 *     },
 *     right = { success: Success<SomeSuccess> ->
 *         buildResource(failure)
 *     },
 * )
 * ```
 */
public inline fun <NF, NS, F, S> NetworkEither<NF, NS>.toResource(
    failure: (Failure<NF>) -> ResourceEither<F, S>,
    success: (NetworkEither.Success<NS>) -> ResourceEither<F, S>,
): ResourceEither<F, S> =
    when (this) {
        is Failure -> failure(this)
        is NetworkEither.Success -> success(this)
    }

/**
 * Map a `NetworkEither` to `ResourceEither` with network `Failure` and `Success` unwrapped as
 * lambda params
 *
 * ```
 * networkEither.toResource(
 *     success = { data: Data, code: Int, headers: Headers ->
 *         buildResource(data, code, headers)
 *     },
 *     httpError = { error: Error, code: Int, headers: Headers ->
 *         buildResource(error, code, headers)
 *     },
 *     localError = { buildResource() },
 *     remoteError = { buildResource() }
 *     unknownError = { unknownError: Throwable -> buildResource(unknownError) }
 * )
 * ```
 */
public inline fun <NF, NS, F, S> NetworkEither<NF, NS>.toResource(
    httpError: (Failure.Http<NF>) -> F,
    localError: () -> F,
    remoteError: () -> F,
    unknownError: (Throwable) -> F,
    success: (NetworkEither.Success<NS>) -> S,
    isLoading: Boolean = false
): ResourceEither<F, S> =
    when (this) {
        is Failure.Http -> resourceFailure(httpError(this), null, isLoading)
        is Failure.Local -> resourceFailure(localError(), null, isLoading)
        is Failure.Remote -> resourceFailure(remoteError(), null, isLoading)
        is Failure.Unknown -> resourceFailure(unknownError(throwable), null, isLoading)
        is NetworkEither.Success -> resourceSuccess(success(this), isLoading)
    }
