package com.javiersc.network.either.arrow

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Failure
import com.javiersc.network.either.NetworkEither.Success

public inline fun <F, S, L, R> NetworkEither<F, S>.toEither(
    crossinline httpFailure: (Failure.Http<F>) -> L,
    localFailure: () -> L,
    remoteFailure: () -> L,
    crossinline unknownFailure: (Throwable) -> L,
    crossinline success: (Success<S>) -> R,
): Either<L, R> =
    fold(
        httpFailure = { f: Failure.Http<F> -> httpFailure(f).left() },
        localFailure = localFailure()::left,
        remoteFailure = remoteFailure()::left,
        unknownFailure = { throwable: Throwable -> unknownFailure(throwable).left() },
        success = { s: Success<S> -> success(s).right() },
    )
