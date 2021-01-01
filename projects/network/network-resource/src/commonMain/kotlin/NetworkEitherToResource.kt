@file:Suppress("LongParameterList")

package com.javiersc.either.network.resource

import com.javiersc.either.Either
import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.buildResourceFailure
import com.javiersc.either.resource.buildResourceSuccess

public fun <NF, NS, F, S> NetworkEither<NF, NS>.toResource(
    success: (NS, Int, Headers) -> S,
    httpError: (NF, Int, Headers) -> F,
    localError: () -> F,
    remoteError: () -> F,
    unknownError: (Throwable) -> F,
    isLoading: Boolean = false
): ResourceEither<F, S> = when (this) {
    is Either.Left -> when (val failure = this.left) {
        is Failure.Http ->
            with(failure) { buildResourceFailure(failure = httpError(error, code, headers), isLoading = isLoading) }
        is Failure.Local -> buildResourceFailure(failure = localError(), isLoading = isLoading)
        is Failure.Remote -> buildResourceFailure(failure = remoteError(), isLoading = isLoading)
        is Failure.Unknown -> buildResourceFailure(failure = unknownError(failure.throwable), isLoading = isLoading)
    }
    is Either.Right -> buildResourceSuccess(
        data = success(this.right.data, this.right.code, this.right.headers),
        isLoading = isLoading,
    )
}

public fun <NF, NS, F, S> NetworkEither<NF, NS>.toResourceSimple(
    success: (NS) -> S,
    httpError: (NF) -> F,
    localError: () -> F,
    remoteError: () -> F,
    unknownError: (Throwable) -> F,
    isLoading: Boolean = false
): ResourceEither<F, S> = when (this) {
    is Either.Left -> when (val failure = this.left) {
        is Failure.Http ->
            with(failure) { buildResourceFailure(failure = httpError(error), isLoading = isLoading) }
        is Failure.Local -> buildResourceFailure(failure = localError(), isLoading = isLoading)
        is Failure.Remote -> buildResourceFailure(failure = remoteError(), isLoading = isLoading)
        is Failure.Unknown -> buildResourceFailure(failure = unknownError(failure.throwable), isLoading = isLoading)
    }
    is Either.Right -> buildResourceSuccess(data = success(this.right.data), isLoading = isLoading)
}

public fun <NF, NS, F, S> NetworkEither<NF, NS>.toResourceSuccessDataErrorCode(
    success: (NS) -> S,
    httpError: (Int) -> F,
    localError: () -> F,
    remoteError: () -> F,
    unknownError: (Throwable) -> F,
    isLoading: Boolean = false
): ResourceEither<F, S> = when (this) {
    is Either.Left -> when (val failure = this.left) {
        is Failure.Http ->
            with(failure) { buildResourceFailure(failure = httpError(code), isLoading = isLoading) }
        is Failure.Local -> buildResourceFailure(failure = localError(), isLoading = isLoading)
        is Failure.Remote -> buildResourceFailure(failure = remoteError(), isLoading = isLoading)
        is Failure.Unknown -> buildResourceFailure(failure = unknownError(failure.throwable), isLoading = isLoading)
    }
    is Either.Right -> buildResourceSuccess(data = success(this.right.data), isLoading = isLoading)
}
