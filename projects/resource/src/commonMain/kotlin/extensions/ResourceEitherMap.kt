package com.javiersc.either.resource.extensions

import com.javiersc.either.Either
import com.javiersc.either.resource.Failure
import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.Success
import com.javiersc.either.resource.Success.Data
import com.javiersc.either.resource.Success.Loading
import com.javiersc.either.resource.buildResourceFailure
import com.javiersc.either.resource.buildResourceSuccessData
import com.javiersc.either.resource.buildResourceSuccessLoading

public fun <F, S, NF, NS> ResourceEither<F, S>.map(
    failure: (F) -> NF,
    fallbackData: (S?) -> NS? = { null },
    data: (S) -> NS,
    isLoading: Boolean? = null
): ResourceEither<NF, NS> = when (this) {
    is Either.Left -> buildResourceFailure(failure(left.failure), fallbackData(left.data), isLoading ?: left.isLoading)
    is Either.Right -> when (val success = right) {
        is Loading -> buildResourceSuccessLoading(fallbackData(success.data))
        is Data -> buildResourceSuccessData(data(success.data), isLoading ?: success.isLoading)
    }
}

public fun <F, S, NF, NS> Failure<F, S>.map(
    failure: (F) -> NF,
    fallbackData: (S?) -> NS? = { null },
    isLoading: Boolean? = null
): Failure<NF, NS> = Failure(failure(this.failure), fallbackData(this.data), isLoading ?: this.isLoading)

public fun <S, NS> Loading<S>.map(fallbackData: (S?) -> NS?): Success<NS> = Loading(fallbackData(data))

public fun <S, NS> Data<S>.map(isLoading: Boolean? = null, data: (S) -> NS): Success<NS> =
    Data(data(this.data), isLoading ?: this.isLoading)
