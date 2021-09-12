package com.javiersc.either.network.extensions

import com.javiersc.either.network.Failure
import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailure
import com.javiersc.either.network.NetworkSuccess
import kotlin.jvm.JvmName

public inline fun <F, S> NetworkEither<F, S>.fold(
    success: (S, HttpStatusCode, Headers) -> Unit,
    failureHttp: (F, HttpStatusCode, Headers) -> Unit,
    failureLocal: () -> Unit,
    failureRemote: () -> Unit,
    failureUnknown: (Throwable) -> Unit,
): Unit =
    when (this) {
        is NetworkFailure<F> ->
            when (val left = left) {
                is Failure.Http<F> -> failureHttp(left.error, left.code, left.headers)
                Failure.Local -> failureLocal()
                Failure.Remote -> failureRemote()
                is Failure.Unknown -> failureUnknown(left.throwable)
            }
        is NetworkSuccess<S> -> success(right.data, right.code, right.headers)
    }

public inline fun <F, S> NetworkEither<F, S>.fold(
    success: (S) -> Unit,
    failureHttp: (F) -> Unit,
    failureLocal: () -> Unit,
    failureRemote: () -> Unit,
    failureUnknown: (Throwable) -> Unit,
): Unit =
    when (this) {
        is NetworkFailure<F> ->
            when (val left = left) {
                is Failure.Http<F> -> failureHttp(left.error)
                Failure.Local -> failureLocal()
                Failure.Remote -> failureRemote()
                is Failure.Unknown -> failureUnknown(left.throwable)
            }
        is NetworkSuccess<S> -> success(right.data)
    }

@JvmName("foldWithSuccessDataAndErrorCode")
public inline fun <F, S> NetworkEither<F, S>.fold(
    success: (S) -> Unit,
    failureHttp: (HttpStatusCode) -> Unit,
    failureLocal: () -> Unit,
    failureRemote: () -> Unit,
    failureUnknown: (Throwable) -> Unit,
): Unit =
    when (this) {
        is NetworkFailure<F> ->
            when (val left = left) {
                is Failure.Http<F> -> failureHttp(left.code)
                Failure.Local -> failureLocal()
                Failure.Remote -> failureRemote()
                is Failure.Unknown -> failureUnknown(left.throwable)
            }
        is NetworkSuccess<S> -> success(right.data)
    }

public inline fun <F, S> NetworkEither<F, S>.foldOnlySuccessData(
    success: (S) -> Unit,
    failureHttp: () -> Unit,
    failureLocal: () -> Unit,
    failureRemote: () -> Unit,
    failureUnknown: (Throwable) -> Unit,
): Unit =
    when (this) {
        is NetworkFailure<F> ->
            when (val left = left) {
                is Failure.Http<F> -> failureHttp()
                Failure.Local -> failureLocal()
                Failure.Remote -> failureRemote()
                is Failure.Unknown -> failureUnknown(left.throwable)
            }
        is NetworkSuccess<S> -> success(right.data)
    }
