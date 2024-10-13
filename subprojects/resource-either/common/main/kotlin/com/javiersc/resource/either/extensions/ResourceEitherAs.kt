package com.javiersc.resource.either.extensions

import com.javiersc.resource.either.ResourceEither
import com.javiersc.resource.either.ResourceEither.Companion.buildResourceFailure
import com.javiersc.resource.either.ResourceEither.Companion.buildResourceSuccess
import com.javiersc.resource.either.ResourceEither.Companion.buildResourceSuccessLoading

/** Transform this in Failure */
public inline fun <reified F, reified S> F.asFailure(
    data: S? = null,
    isLoading: Boolean = false,
): ResourceEither<F, S> = buildResourceFailure(this, data, isLoading)

/** Transform this in SuccessLoading */
public inline fun <reified F, reified S> S.asSuccessLoading(): ResourceEither<F, S> =
    buildResourceSuccessLoading(this)

/** Transform this in Success */
public inline fun <reified F, reified S> S.asSuccess(
    isLoading: Boolean = false
): ResourceEither<F, S> = buildResourceSuccess(this, isLoading)
