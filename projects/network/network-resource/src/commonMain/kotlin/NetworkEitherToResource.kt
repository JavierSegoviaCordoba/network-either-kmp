package com.javiersc.either.network.resource

import com.javiersc.either.Either
import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.buildResourceFailure
import com.javiersc.either.resource.buildResourceSuccessData

public fun <NF, NS, F, S> NetworkEither<NF, NS>.toResource(
    success: (NS) -> S,
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
    is Either.Right -> buildResourceSuccessData(data = success(this.right.data), isLoading = isLoading)
}
