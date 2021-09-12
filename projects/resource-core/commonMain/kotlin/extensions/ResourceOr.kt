package com.javiersc.either.resource.extensions

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.ResourceSuccess

public fun <F, S> ResourceEither<F, S>.successOrNull(): ResourceSuccess<S>? =
    if (this is ResourceSuccess<S>) this else null

public fun <F, S> ResourceEither<F, S>.successDataOrNull(): S? =
    if (this is ResourceSuccess<S>) right.data else null
