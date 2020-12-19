package com.javiersc.either.extensions

import com.javiersc.either.Either

/**
 * Map an Either to another completely different Either
 */
public fun <L, R, NL, NR> Either<L, R>.map(
    left: (L) -> NL,
    right: (R) -> NR,
): Either<NL, NR> = when (this) {
    is Either.Left -> Either.Left(left(this.left))
    is Either.Right -> Either.Right(right(this.right))
}

/**
 * Map left part of an Either with the same Right type
 */
public fun <L, R, NL> Either<L, R>.mapLeft(
    left: (L) -> NL,
): Either<NL, R> = when (this) {
    is Either.Left -> Either.Left(left(this.left))
    is Either.Right -> this
}

/**
 * Map right part of an Either with the same Left type
 */
public fun <L, R, NR> Either<L, R>.mapRight(
    right: (R) -> NR,
): Either<L, NR> = when (this) {
    is Either.Left -> this
    is Either.Right -> Either.Right(right(this.right))
}
