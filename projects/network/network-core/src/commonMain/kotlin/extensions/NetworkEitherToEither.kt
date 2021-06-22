package com.javiersc.either.network.extensions

import com.javiersc.either.Either
import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.Success
import kotlin.jvm.JvmName

/**
 * Map a `NetworkEither` to `Either` where the lambda params are the network `Failure` and
 * `Success`, directly.
 *
 * ```
 * networkEither.map(
 *     left = { failure: Failure<SomeFailure> -> buildEither(failure) },
 *     right = { success: Success<SomeSuccess> -> buildEither(success) },
 * )
 * ```
 */
public fun <F, S, L, R> NetworkEither<F, S>.toEither(
    left: (Failure<F>) -> L,
    right: (Success<S>) -> R,
): Either<L, R> =
    when (this) {
        is Either.Left -> Either.Left(left(this.left))
        is Either.Right -> Either.Right(right(this.right))
    }

/**
 * Map a `NetworkEither` to `Either` where the lambda params are the network `Failure` and `Success`
 * unwrapped
 *
 * ```
 * networkEither.map(
 *     success = { data: SomeSuccess, code: HttpStatusCode, headers: Headers -> buildEither(data, code, headers) },
 *     httpError = { error: SomeFailure, code: HttpStatusCode, headers: Headers -> buildEither(error, code, headers) },
 *     localError = { buildEither() },
 *     remoteError = { buildEither() }
 *     unknownError = { unknownError: Throwable -> buildEither(unknownError) }
 * )
 * ```
 */
public fun <F, S, L, R> NetworkEither<F, S>.toEither(
    success: (S, HttpStatusCode, Headers) -> R,
    httpError: (F, HttpStatusCode, Headers) -> L,
    localError: () -> L,
    remoteError: () -> L,
    unknownError: (Throwable) -> L,
): Either<L, R> =
    when (this) {
        is Either.Left ->
            when (val failure = left) {
                is Failure.Http -> with(failure) { Either.Left(httpError(error, code, headers)) }
                is Failure.Local -> Either.Left(localError())
                is Failure.Remote -> Either.Left(remoteError())
                is Failure.Unknown -> Either.Left(unknownError(failure.throwable))
            }
        is Either.Right -> with(right) { Either.Right(success(data, code, headers)) }
    }

/**
 * Map a `NetworkEither` to `Either` where the lambda params are the network `Failure` data and the
 * `Success` data
 *
 * ```
 * networkEither.map(
 *     success = { data: SomeSuccess, code: HttpStatusCode, headers: Headers -> buildEither(data, code, headers) },
 *     httpError = { error: SomeFailure, code: HttpStatusCode, headers: Headers -> buildEither(error, code, headers) },
 *     localError = { buildEither() },
 *     remoteError = { buildEither() }
 *     unknownError = { unknownError: Throwable -> buildEither(unknownError) }
 * )
 * ```
 */
public fun <F, S, L, R> NetworkEither<F, S>.toEither(
    success: (S) -> R,
    httpError: (F) -> L,
    localError: () -> L,
    remoteError: () -> L,
    unknownError: (Throwable) -> L,
): Either<L, R> =
    when (this) {
        is Either.Left ->
            when (val failure = left) {
                is Failure.Http -> with(failure) { Either.Left(httpError(error)) }
                is Failure.Local -> Either.Left(localError())
                is Failure.Remote -> Either.Left(remoteError())
                is Failure.Unknown -> Either.Left(unknownError(failure.throwable))
            }
        is Either.Right -> with(right) { Either.Right(success(data)) }
    }

/**
 * Map a `NetworkEither` to `Either` where the lambda params are the network `Failure` code and the
 * `Success` data
 *
 * ```
 * networkEither.map(
 *     success = { data: SomeSuccess, code: HttpStatusCode, headers: Headers -> buildEither(data, code, headers) },
 *     httpError = { error: SomeFailure, code: HttpStatusCode, headers: Headers -> buildEither(error, code, headers) },
 *     localError = { buildEither() },
 *     remoteError = { buildEither() }
 *     unknownError = { unknownError: Throwable -> buildEither(unknownError) }
 * )
 * ```
 */
@JvmName("toEitherWithDataAndErrorCode")
public fun <F, S, L, R> NetworkEither<F, S>.toEither(
    success: (S) -> R,
    httpError: (HttpStatusCode) -> L,
    localError: () -> L,
    remoteError: () -> L,
    unknownError: (Throwable) -> L,
): Either<L, R> =
    when (this) {
        is Either.Left ->
            when (val failure = left) {
                is Failure.Http -> with(failure) { Either.Left(httpError(code)) }
                is Failure.Local -> Either.Left(localError())
                is Failure.Remote -> Either.Left(remoteError())
                is Failure.Unknown -> Either.Left(unknownError(failure.throwable))
            }
        is Either.Right -> with(right) { Either.Right(success(data)) }
    }
