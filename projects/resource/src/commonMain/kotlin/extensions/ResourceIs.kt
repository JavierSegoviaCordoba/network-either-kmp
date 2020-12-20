package com.javiersc.either.resource.extensions

import com.javiersc.either.Either
import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.Success.Data
import com.javiersc.either.resource.Success.Loading

public val <F, S> ResourceEither<F, S>.isFailure: Boolean get() = this is Either.Left

public val <F, S> ResourceEither<F, S>.isSuccessLoading: Boolean get() = (this is Either.Right && right is Loading)

public val <F, S> ResourceEither<F, S>.isSuccessData: Boolean get() = (this is Either.Right && right is Data)

public val <F, S> ResourceEither<F, S>.isLoading: Boolean
    get() = (this is Either.Left && left.isLoading) ||
            (this is Either.Right && right is Loading) ||
            (this is Either.Right && right is Data && (right as Data<S>).isLoading)
