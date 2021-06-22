package com.javiersc.either.network.extensions

import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailure
import com.javiersc.either.network.NetworkSuccess

public suspend fun <F, S> NetworkEither<F, S>.ifFailureSuspended(
    block: suspend (Failure<F>) -> Unit
) {
    if (this is NetworkFailure<F>) block(left)
}

public suspend fun <F, S> NetworkEither<F, S>.ifFailureHttpSuspended(
    block: suspend (F, HttpStatusCode, Headers) -> Unit
) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(error, code, headers) }
    }
}

public suspend fun <F, S> NetworkEither<F, S>.ifFailureHttpOnlyErrorSuspended(
    block: suspend (F) -> Unit
) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(error) }
    }
}

public suspend fun <F, S> NetworkEither<F, S>.ifFailureHttpOnlyCodeSuspended(
    block: suspend (HttpStatusCode) -> Unit
) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(code) }
    }
}

public suspend fun <F, S> NetworkEither<F, S>.ifFailureLocalSuspended(block: suspend () -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Local) block()
}

public suspend fun <F, S> NetworkEither<F, S>.ifFailureRemoteSuspended(block: suspend () -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Remote) block()
}

public suspend fun <F, S> NetworkEither<F, S>.ifFailureUnknownSuspended(
    block: suspend (Throwable) -> Unit
) {
    if (this is NetworkFailure<F> && left is Failure.Unknown) {
        block((left as Failure.Unknown).throwable)
    }
}

public suspend fun <F, S> NetworkEither<F, S>.ifSuccessSuspended(
    block: suspend (S, HttpStatusCode, Headers) -> Unit
) {
    if (this is NetworkSuccess<S>) with(right) { block(data, code, headers) }
}

public suspend fun <F, S> NetworkEither<F, S>.ifSuccessOnlyDataSuspended(
    block: suspend (S) -> Unit
) {
    if (this is NetworkSuccess<S>) with(right) { block(data) }
}
