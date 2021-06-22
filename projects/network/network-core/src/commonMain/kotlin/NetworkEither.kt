package com.javiersc.either.network

import com.javiersc.either.Either

public typealias NetworkEither<F, S> = Either<Failure<F>, Success<S>>

public typealias NetworkFailure<F> = Either.Left<Failure<F>>

public typealias NetworkFailureHttp<F> = Either.Left<Failure.Http<F>>

public typealias NetworkFailureLocal = Either.Left<Failure.Local>

public typealias NetworkFailureRemote = Either.Left<Failure.Remote>

public typealias NetworkFailureUnknown = Either.Left<Failure.Unknown>

public sealed class Failure<out F> {

    public data class Http<F>(val error: F, val code: HttpStatusCode, val headers: Headers) :
        Failure<F>()

    public object Local : Failure<Nothing>()

    public object Remote : Failure<Nothing>()

    public data class Unknown(val throwable: Throwable) : Failure<Nothing>()
}

public typealias NetworkSuccess<S> = Either.Right<Success<S>>

public data class Success<S>(val data: S, val code: HttpStatusCode, val headers: Headers)

public typealias Headers = Map<String, List<String>>

public fun <F, S> buildNetworkFailureHttp(
    error: F,
    code: HttpStatusCode,
    headers: Headers
): NetworkEither<F, S> = Either.Left(Failure.Http(error = error, code = code, headers = headers))

public fun <F, S> buildNetworkFailureLocal(): NetworkEither<F, S> = Either.Left(Failure.Local)

public fun <F, S> buildNetworkFailureRemote(): NetworkEither<F, S> = Either.Left(Failure.Remote)

public fun <F, S> buildNetworkFailureUnknown(throwable: Throwable): NetworkEither<F, S> =
    Either.Left(Failure.Unknown(throwable = throwable))

public fun <F, S> buildNetworkSuccess(
    data: S,
    code: HttpStatusCode,
    headers: Headers
): NetworkEither<F, S> = Either.Right(Success(data = data, code = code, headers = headers))

public data class HttpStatusCode(public val value: Int)
