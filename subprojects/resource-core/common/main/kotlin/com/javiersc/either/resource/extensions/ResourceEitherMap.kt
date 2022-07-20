package com.javiersc.either.resource.extensions

import com.javiersc.either.Either
import com.javiersc.either.resource.Failure
import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.Success
import com.javiersc.either.resource.buildResourceFailure
import com.javiersc.either.resource.buildResourceSuccess

public inline fun <F, S, NF, NS> ResourceEither<F, S>.map(
    failure: (F) -> NF,
    fallbackData: (S?) -> NS? = { null },
    success: (S) -> NS,
    isLoading: Boolean? = null
): ResourceEither<NF, NS> =
    when (this) {
        is Either.Left ->
            buildResourceFailure(
                failure(left.failure),
                fallbackData(left.data),
                isLoading ?: left.isLoading
            )
        is Either.Right -> buildResourceSuccess(success(right.data), isLoading ?: right.isLoading)
    }

public inline fun <F, S, NF, NS> Failure<F, S>.map(
    failure: (F) -> NF,
    fallbackData: (S?) -> NS? = { null },
    isLoading: Boolean? = null
): Failure<NF, NS> =
    Failure(failure(this.failure), fallbackData(this.data), isLoading ?: this.isLoading)

public inline fun <S, NS> Success<S>.map(isLoading: Boolean? = null, data: (S) -> NS): Success<NS> =
    Success(data(this.data), isLoading ?: this.isLoading)
