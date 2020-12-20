package com.javiersc.either.resource

/**
 * Right value for Either which lets you add the success data and a loading flag
 */
public sealed class Success<out S> {
    public data class Loading<S>(val data: S?) : Success<S>()
    public data class Data<S>(val data: S, val isLoading: Boolean = false) : Success<S>()
}
