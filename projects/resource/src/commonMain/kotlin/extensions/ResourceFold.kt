package com.javiersc.either.resource.extensions

import com.javiersc.either.Either
import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.Success.Data
import com.javiersc.either.resource.Success.Loading

public fun <F, S> ResourceEither<F, S>.fold(
    failure: (F, S?, Boolean) -> Unit,
    data: (S, Boolean) -> Unit,
    loading: (S?) -> Unit = { },
): Unit = when (this) {
    is Either.Left -> failure(left.failure, left.data, left.isLoading)
    is Either.Right -> when (val success = right) {
        is Loading -> loading(success.data)
        is Data -> data(success.data, success.isLoading)
    }
}

public fun <F, S> ResourceEither<F, S>.ifFailure(failure: (F, S?, Boolean) -> Unit) {
    if (this is Either.Left) failure(left.failure, left.data, left.isLoading)
}

public fun <F, S> ResourceEither<F, S>.ifSuccessLoading(loading: (S?) -> Unit) {
    if (this is Either.Right && right is Loading<S>) loading((right as Loading<S>).data)
}

public fun <F, S> ResourceEither<F, S>.ifSuccessData(data: (S) -> Unit) {
    if (this is Either.Right && right is Data<S>) data((right as Data<S>).data)
}
