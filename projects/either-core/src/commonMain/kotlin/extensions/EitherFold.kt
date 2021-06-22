package com.javiersc.either.extensions

import com.javiersc.either.Either

/** Fold an Either with left and right callbacks */
public fun <L, R> Either<L, R>.fold(left: (L) -> Unit, right: (R) -> Unit): Unit =
    when (this) {
        is Either.Left -> left(this.left)
        is Either.Right -> right(this.right)
    }
