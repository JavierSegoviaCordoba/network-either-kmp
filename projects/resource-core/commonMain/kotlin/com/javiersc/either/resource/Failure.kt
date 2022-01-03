package com.javiersc.either.resource

/**
 * Left value fot Either which lets you add the failure data, a fallback success data and a loading
 * flag
 */
public data class Failure<F, S>(
    val failure: F,
    val data: S? = null,
    val isLoading: Boolean = false
)
