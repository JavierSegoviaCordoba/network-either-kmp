package com.javiersc.either.resource

/**
 * Right value for Either which lets you add the success data and a loading flag
 */
public data class Success<out S>(val data: S, val isLoading: Boolean = false)
