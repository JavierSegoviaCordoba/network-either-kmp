package com.javiersc.either

/**
 * Left is usually used to represent the Failure or Error Right is usually used to represent the
 * Success
 */
public sealed class Either<out L, out R> {
    public data class Left<out L>(val left: L) : Either<L, Nothing>()
    public data class Right<out R>(val right: R) : Either<Nothing, R>()
}
