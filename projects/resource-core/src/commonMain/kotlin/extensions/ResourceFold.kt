package com.javiersc.either.resource.extensions

import com.javiersc.either.Either
import com.javiersc.either.resource.ResourceEither

public fun <F, S> ResourceEither<F, S>.fold(
    failure: (F, S?, Boolean) -> Unit,
    success: (S, Boolean) -> Unit,
): Unit = when (this) {
    is Either.Left -> failure(left.failure, left.data, left.isLoading)
    is Either.Right -> success(right.data, right.isLoading)
}

public fun <F, S> ResourceEither<F, S>.ifFailure(failure: (F, S?, Boolean) -> Unit) {
    if (this is Either.Left) failure(left.failure, left.data, left.isLoading)
}

public fun <F, S> ResourceEither<F, S>.ifSuccess(data: (S, Boolean) -> Unit) {
    if (this is Either.Right) data(right.data, right.isLoading)
}
