package com.javiersc.either.resource.extensions

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.buildResourceFailure
import com.javiersc.either.resource.buildResourceSuccessData
import com.javiersc.either.resource.buildResourceSuccessLoading

/**
 * Transform this in Failure
 */
public fun <F, S> F.asFailure(data: S? = null, isLoading: Boolean = false): ResourceEither<F, S> =
    buildResourceFailure(this, data, isLoading)

/**
 * Transform this in SuccessLoading
 */
public fun <F, S> S.asSuccessLoading(): ResourceEither<F, S> = buildResourceSuccessLoading(this)

/**
 * Transform this in SuccessData
 */
public fun <F, S> S.asSuccessData(isLoading: Boolean = false): ResourceEither<F, S> =
    buildResourceSuccessData(this, isLoading)
