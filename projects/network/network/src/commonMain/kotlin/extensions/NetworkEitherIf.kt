package com.javiersc.either.network.extensions

import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailure
import com.javiersc.either.network.NetworkSuccess
import kotlin.jvm.JvmName

public fun <F, S> NetworkEither<F, S>.ifFailure(block: (Failure<F>) -> Unit) {
    if (this is NetworkFailure<F>) block(left)
}

public fun <F, S> NetworkEither<F, S>.ifFailureHttp(block: (F, Int, Headers) -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(error, code, headers) }
    }
}

@JvmName("ifFailureHttpError")
public fun <F, S> NetworkEither<F, S>.ifFailureHttp(block: (F) -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(error) }
    }
}

@JvmName("ifFailureHttpCode")
public fun <F, S> NetworkEither<F, S>.ifFailureHttp(block: (Int) -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Http<F>) {
        with(left as Failure.Http<F>) { block(code) }
    }
}

public fun <F, S> NetworkEither<F, S>.ifFailureLocal(block: () -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Local) block()
}

public fun <F, S> NetworkEither<F, S>.ifFailureRemote(block: () -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Remote) block()
}

public fun <F, S> NetworkEither<F, S>.ifFailureUnknown(block: (Throwable) -> Unit) {
    if (this is NetworkFailure<F> && left is Failure.Unknown) block((left as Failure.Unknown).throwable)
}

public fun <F, S> NetworkEither<F, S>.ifSuccess(block: (S, Int, Headers) -> Unit) {
    if (this is NetworkSuccess<S>) with(right) { block(data, code, headers) }
}

@JvmName("ifSuccessData")
public fun <F, S> NetworkEither<F, S>.ifSuccess(block: (S) -> Unit) {
    if (this is NetworkSuccess<S>) with(right) { block(data) }
}
