@file:Suppress("FunctionName")

package com.javiersc.either.resource

import com.javiersc.either.Either

/**
 * Custom Either which uses Success and Failure classes
 */
public typealias ResourceEither<F, S> = Either<Failure<F, S>, Success<S>>

public typealias ResourceFailure<F, S> = Either.Left<Failure<F, S>>

public typealias ResourceSuccess<S> = Either.Right<Success<S>>

public typealias ResourceSuccessLoading<S> = Either.Right<Success.Loading<S>>

public typealias ResourceSuccessData<S> = Either.Right<Success.Data<S>>

public fun <F, S> buildResourceFailure(failure: F, data: S? = null, isLoading: Boolean = false): ResourceEither<F, S> =
    Either.Left(Failure(failure, data, isLoading))

public fun <F, S> buildResourceSuccessLoading(data: S?): ResourceEither<F, S> = Either.Right(Success.Loading(data))

public fun <F, S> buildResourceSuccessData(data: S, isLoading: Boolean = false): ResourceEither<F, S> =
    Either.Right(Success.Data(data, isLoading))
