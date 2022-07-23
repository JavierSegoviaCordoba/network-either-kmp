@file:Suppress("FunctionName")

package com.javiersc.resource.either

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlin.contracts.contract

public sealed class ResourceEither<out F, out S> {

    public abstract val isLoading: Boolean

    public data class Failure<out F, out S>(
        val failure: F,
        val data: S? = null,
        override val isLoading: Boolean = false
    ) : ResourceEither<F, S>()

    public data class Success<out S>(
        val data: S,
        override val isLoading: Boolean = false,
    ) : ResourceEither<Nothing, S>()

    public inline fun <A> fold(
        failure: (Failure<F, S>) -> A,
        success: (Success<S>) -> A,
    ): A =
        when (this) {
            is Failure -> failure(this)
            is Success -> success(this)
        }

    public inline fun ifFailure(block: (Failure<F, S>) -> Unit) {
        if (this is Failure) block(this)
    }

    public inline fun ifSuccess(block: (Success<S>) -> Unit) {
        if (this is Success) block(this)
    }

    public fun isFailure(): Boolean {
        contract { returns(true) implies (this@ResourceEither is Failure<F, S>) }
        return this is Failure<F, S>
    }

    public fun isSuccess(): Boolean {
        contract { returns(true) implies (this@ResourceEither is Success<S>) }
        return this is Success<S>
    }

    public inline fun <L, R> toEither(
        failure: (Failure<F, S>) -> L,
        success: (Success<S>) -> R,
    ): Either<L, R> =
        fold(
            failure = { failure(it).left() },
            success = { success(it).right() },
        )

    @JvmName("getIsLoading")
    public fun isLoading(): Boolean {
        return this.isLoading
    }

    public fun successOrNull(): ResourceSuccess<S>? {
        contract { returnsNotNull() implies (this@ResourceEither is ResourceSuccess) }
        return (this as? ResourceSuccess<S>)
    }

    public fun successDataOrNull(): S? {
        contract { returnsNotNull() implies (this@ResourceEither is ResourceSuccess) }
        return (this as? ResourceSuccess<S>)?.data
    }

    public companion object {
        public fun <F, S> resourceFailure(
            failure: F,
            data: S? = null,
            isLoading: Boolean = false
        ): ResourceEither<F, S> = Failure(failure, data, isLoading)

        public fun <F, S> buildResourceFailure(
            failure: F,
            data: S? = null,
            isLoading: Boolean = false
        ): Failure<F, S> = Failure(failure, data, isLoading)

        public fun <F, S> resourceFailureLoading(
            failure: F,
            data: S? = null
        ): ResourceEither<F, S> = Failure(failure, data, true)

        public fun <F, S> buildResourceFailureLoading(failure: F, data: S? = null): Failure<F, S> =
            Failure(failure, data, true)

        public fun <S> resourceSuccess(
            data: S,
            isLoading: Boolean = false
        ): ResourceEither<Nothing, S> = Success(data, isLoading)

        public fun <S> buildResourceSuccess(data: S, isLoading: Boolean = false): Success<S> =
            Success(data, isLoading)

        public fun <S> resourceSuccessLoading(data: S): ResourceEither<Nothing, S> =
            Success(data, true)

        public fun <S> buildResourceSuccessLoading(data: S): Success<S> = Success(data, true)
    }
}

public typealias ResourceFailure<F, S> = ResourceEither.Failure<F, S>

public typealias ResourceSuccess<S> = ResourceEither.Success<S>
