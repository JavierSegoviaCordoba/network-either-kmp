package com.javiersc.either.network.extensions

import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailure
import com.javiersc.either.network.NetworkSuccess

public suspend fun <F, S> NetworkEither<F, S>.suspendFold(
    success: suspend (S, Int, Headers) -> Unit,
    failureHttp: suspend (F, Int, Headers) -> Unit,
    failureLocal: suspend () -> Unit,
    failureRemote: suspend () -> Unit,
    failureUnknown: suspend (Throwable) -> Unit,
): Unit = when (this) {
    is NetworkFailure<F> -> when (val left = left) {
        is Failure.Http<F> -> failureHttp(left.error, left.code, left.headers)
        Failure.Local -> failureLocal()
        Failure.Remote -> failureRemote()
        is Failure.Unknown -> failureUnknown(left.throwable)
    }
    is NetworkSuccess<S> -> success(right.data, right.code, right.headers)
}

public suspend fun <F, S> NetworkEither<F, S>.suspendFoldSimple(
    success: suspend (S) -> Unit,
    failureHttp: suspend (F) -> Unit,
    failureLocal: suspend () -> Unit,
    failureRemote: suspend () -> Unit,
    failureUnknown: suspend (Throwable) -> Unit,
): Unit = when (this) {
    is NetworkFailure<F> -> when (val left = left) {
        is Failure.Http<F> -> failureHttp(left.error)
        Failure.Local -> failureLocal()
        Failure.Remote -> failureRemote()
        is Failure.Unknown -> failureUnknown(left.throwable)
    }
    is NetworkSuccess<S> -> success(right.data)
}

public suspend fun <F, S> NetworkEither<F, S>.suspendFoldSuccessDataErrorCode(
    success: suspend (S) -> Unit,
    failureHttp: suspend (Int) -> Unit,
    failureLocal: suspend () -> Unit,
    failureRemote: suspend () -> Unit,
    failureUnknown: suspend (Throwable) -> Unit,
): Unit = when (this) {
    is NetworkFailure<F> -> when (val left = left) {
        is Failure.Http<F> -> failureHttp(left.code)
        Failure.Local -> failureLocal()
        Failure.Remote -> failureRemote()
        is Failure.Unknown -> failureUnknown(left.throwable)
    }
    is NetworkSuccess<S> -> success(right.data)
}

public suspend fun <F, S> NetworkEither<F, S>.suspendFoldOnlySuccessData(
    success: suspend (S) -> Unit,
    failureHttp: suspend () -> Unit,
    failureLocal: suspend () -> Unit,
    failureRemote: suspend () -> Unit,
    failureUnknown: suspend (Throwable) -> Unit,
): Unit = when (this) {
    is NetworkFailure<F> -> when (val left = left) {
        is Failure.Http<F> -> failureHttp()
        Failure.Local -> failureLocal()
        Failure.Remote -> failureRemote()
        is Failure.Unknown -> failureUnknown(left.throwable)
    }
    is NetworkSuccess<S> -> success(right.data)
}
