package com.javiersc.network.either

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlin.contracts.contract
import kotlin.jvm.JvmName

public sealed class NetworkEither<out F, out S> {

    public sealed class Failure<out F> : NetworkEither<F, Nothing>() {

        public data class Http<out F>(val error: F, val code: Int, val headers: Headers) :
            Failure<F>()

        public data object Local : Failure<Nothing>()

        public data object Remote : Failure<Nothing>()

        public data class Unknown(val throwable: Throwable) : Failure<Nothing>()
    }

    public data class Success<out S>(val data: S, val code: Int, val headers: Headers) :
        NetworkEither<Nothing, S>()

    public inline fun <A> fold(failure: () -> A, success: (S) -> A): A =
        when (this) {
            is Failure.Http<F> -> failure()
            is Failure.Local -> failure()
            is Failure.Remote -> failure()
            is Failure.Unknown -> failure()
            is Success -> success(data)
        }

    public inline fun <A> fold(
        httpFailure: (Failure.Http<F>) -> A,
        localFailure: () -> A,
        remoteFailure: () -> A,
        unknownFailure: (Throwable) -> A,
        success: (Success<S>) -> A,
    ): A =
        when (this) {
            is Failure.Http<F> -> httpFailure(this)
            is Failure.Local -> localFailure()
            is Failure.Remote -> remoteFailure()
            is Failure.Unknown -> unknownFailure(throwable)
            is Success -> success(this)
        }

    public fun isFailure(): Boolean {
        contract { returns(true) implies (this@NetworkEither is Failure<F>) }
        return this is Failure<F>
    }

    public fun isHttpFailure(): Boolean {
        contract { returns(true) implies (this@NetworkEither is Failure.Http<F>) }
        return this is Failure.Http<F>
    }

    public fun isLocalFailure(): Boolean {
        contract { returns(true) implies (this@NetworkEither is Failure.Local) }
        return this is Failure.Local
    }

    public fun isRemoteFailure(): Boolean {
        contract { returns(true) implies (this@NetworkEither is Failure.Remote) }
        return this is Failure.Remote
    }

    public fun isUnknownFailure(): Boolean {
        contract { returns(true) implies (this@NetworkEither is Failure.Unknown) }
        return this is Failure.Unknown
    }

    public fun isSuccess(): Boolean {
        contract { returns(true) implies (this@NetworkEither is Success<S>) }
        return this is Success<S>
    }

    @JvmName("getCode1")
    public fun getCode(): Int =
        when (this) {
            is Failure.Http -> this.code
            is Success -> this.code
            else -> error("There is no response with code")
        }

    public fun getCodeOrNull(): Int? = runCatching { getCode() }.getOrNull()

    public fun getSuccess(): S {
        contract { returns() implies (this@NetworkEither is Success) }
        return (this as Success<S>).data
    }

    public fun getSuccessOrNull(): S? {
        contract { returnsNotNull() implies (this@NetworkEither is Success) }
        return (this as? Success<S>)?.data
    }

    @JvmName("getThrowable1")
    public fun getThrowable(): Throwable {
        contract { returns() implies (this@NetworkEither is Failure.Unknown) }
        return (this as Failure.Unknown).throwable
    }

    public fun getThrowableOrNull(): Throwable? {
        contract { returnsNotNull() implies (this@NetworkEither is Failure.Unknown) }
        return (this as? Failure.Unknown)?.throwable
    }

    public inline fun <L, R> toEither(
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

    public fun ifFailure(block: (Failure<F>) -> Unit) {
        if (this is Failure<F>) block(this)
    }

    public fun ifFailureHttp(block: (Failure.Http<F>) -> Unit) {
        if (this is Failure.Http<F>) block(this)
    }

    public fun ifFailureLocal(block: () -> Unit) {
        if (this is Failure.Local) block()
    }

    public fun ifFailureRemote(block: () -> Unit) {
        if (this is Failure.Remote) block()
    }

    public fun ifFailureUnknown(block: (Throwable) -> Unit) {
        if (this is Failure.Unknown) block(throwable)
    }

    public fun ifSuccess(block: (Success<S>) -> Unit) {
        if (this is Success<S>) block(this)
    }

    public companion object {
        public fun <S> buildSuccess(data: S, code: Int, headers: Headers): Success<S> =
            Success(data, code, headers)

        public fun <S> success(data: S, code: Int, headers: Headers): NetworkEither<Nothing, S> =
            Success(data, code, headers)

        public fun <F> buildHttpFailure(error: F, code: Int, headers: Headers): Failure.Http<F> =
            Failure.Http(error, code, headers)

        public fun <F> httpFailure(
            error: F,
            code: Int,
            headers: Headers,
        ): NetworkEither<F, Nothing> = Failure.Http(error, code, headers)

        public fun buildLocalFailure(): Failure.Local = Failure.Local

        public fun localFailure(): NetworkEither<Nothing, Nothing> = Failure.Local

        public fun buildRemoteFailure(): Failure.Remote = Failure.Remote

        public fun remoteFailure(): NetworkEither<Nothing, Nothing> = Failure.Remote

        public fun buildUnknownFailure(throwable: Throwable): Failure.Unknown =
            Failure.Unknown(throwable)

        public fun unknownFailure(throwable: Throwable): NetworkEither<Nothing, Nothing> =
            Failure.Unknown(throwable)
    }
}

public typealias NetworkFailure<F> = NetworkEither.Failure<F>

public typealias NetworkFailureHttp<F> = NetworkEither.Failure.Http<F>

public typealias NetworkFailureLocal = NetworkEither.Failure.Local

public typealias NetworkFailureRemote = NetworkEither.Failure.Remote

public typealias NetworkFailureUnknown = NetworkEither.Failure.Unknown

public typealias NetworkSuccess<S> = NetworkEither.Success<S>

public typealias Headers = Map<String, List<String>>
