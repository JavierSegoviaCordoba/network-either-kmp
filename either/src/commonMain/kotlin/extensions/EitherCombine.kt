package com.javiersc.either.extensions

import com.javiersc.either.Either

/**
 * Combine an Either with another Either where both and the resulting Either have the same type
 */
public fun <L, R> Either<L, R>.combine(
    either: Either<L, R>,
    left: (L, L) -> L,
    right: (R, R) -> R,
): Either<L, R> = when (this) {
    is Either.Left -> when (either) {
        is Either.Left -> Either.Left(left(this.left, either.left))
        is Either.Right -> this
    }
    is Either.Right -> when (either) {
        is Either.Left -> either
        is Either.Right -> Either.Right(right(this.right, either.right))
    }
}

/**
 * Combine an Either with another Either where all Either have different types
 */
public fun <L1, R1, L2, R2, R, L> Either<L1, R1>.combine(
    another: Either<L2, R2>,
    left: (L1, L2) -> L,
    thisLeft: (L1) -> L,
    anotherLeft: (L2) -> L,
    right: (R1, R2) -> R,
): Either<L, R> = when (this) {
    is Either.Left -> when (another) {
        is Either.Left -> Either.Left(left(this.left, another.left))
        is Either.Right -> Either.Left(thisLeft(this.left))
    }
    is Either.Right -> when (another) {
        is Either.Left -> Either.Left(anotherLeft(another.left))
        is Either.Right -> Either.Right(right(this.right, another.right))
    }
}

/**
 * Combine an Either with another Either where all Either have different right type and same left type
 */
public fun <L, R1, R2, R> Either<L, R1>.combine(
    either: Either<L, R2>,
    left: (L, L) -> L,
    right: (R1, R2) -> R,
): Either<L, R> = when (this) {
    is Either.Left -> when (either) {
        is Either.Left -> Either.Left(left(this.left, either.left))
        is Either.Right -> this
    }
    is Either.Right -> when (either) {
        is Either.Left -> either
        is Either.Right -> Either.Right(right(this.right, either.right))
    }
}
