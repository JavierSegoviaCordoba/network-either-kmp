package com.javiersc.either.extensions

import com.javiersc.either.Either

/** Transform this as Either.Left */
public fun <L, R> L.asLeft(): Either<L, R> = Either.Left(this)

/** Transform this as Either.Right */
public fun <L, R> R.asRight(): Either<L, R> = Either.Right(this)

/** Transform first as Either.Left */
public fun <L, R> Pair<L, R>.asLeft(): Either<L, R> = Either.Left(first)

/** Transform second as Either.Right */
public fun <L, R> Pair<L, R>.asRight(): Either<L, R> = Either.Right(second)
