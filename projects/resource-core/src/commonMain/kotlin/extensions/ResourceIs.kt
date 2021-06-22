package com.javiersc.either.resource.extensions

import com.javiersc.either.Either
import com.javiersc.either.resource.ResourceEither

public val <F, S> ResourceEither<F, S>.isFailure: Boolean
    get() = this is Either.Left

public val <F, S> ResourceEither<F, S>.isSuccess: Boolean
    get() = (this is Either.Right)

public val <F, S> ResourceEither<F, S>.isLoading: Boolean
    get() = (this is Either.Left && left.isLoading) || (this is Either.Right && right.isLoading)
