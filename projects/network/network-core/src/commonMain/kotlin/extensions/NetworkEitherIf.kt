package com.javiersc.either.network.extensions

import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailure
import com.javiersc.either.network.NetworkSuccess

public inline fun <F, S> NetworkEither<F, S>.ifFailure(block: (Failure<F>) -> Unit) {
    if (this is NetworkFailure<F>) block(left)
}

public inline fun <F, S> NetworkEither<F, S>.ifFailureHttp(
    block: (F, HttpStatusCode, Headers) -> Unit
) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(error, code, headers) }
    }
}

public inline fun <F, S> NetworkEither<F, S>.ifFailureHttpOnlyError(block: (F) -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(error) }
    }
}

public inline fun <F, S> NetworkEither<F, S>.ifFailureHttpOnlyCode(
    block: (HttpStatusCode) -> Unit
) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(code) }
    }
}

public inline fun <F, S> NetworkEither<F, S>.ifFailureLocal(block: () -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Local) block()
}

public inline fun <F, S> NetworkEither<F, S>.ifFailureRemote(block: () -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Remote) block()
}

public inline fun <F, S> NetworkEither<F, S>.ifFailureUnknown(block: (Throwable) -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Unknown) {
        block((left as Failure.Unknown).throwable)
    }
}

public inline fun <F, S> NetworkEither<F, S>.ifSuccess(
    block: (S, HttpStatusCode, Headers) -> Unit
) {
    if (this is NetworkSuccess<S>) with(right) { block(data, code, headers) }
}

public inline fun <F, S> NetworkEither<F, S>.ifSuccessOnlyData(block: (S) -> Unit) {
    if (this is NetworkSuccess<S>) with(right) { block(data) }
}
