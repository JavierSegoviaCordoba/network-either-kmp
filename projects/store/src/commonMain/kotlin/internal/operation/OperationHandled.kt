package com.javiersc.either.store.internal.operation

import com.javiersc.either.resource.ResourceEither

internal data class OperationHandled<F, S>(
    val cache: S? = null,
    val remote: ResourceEither<F, S>? = null,
    val persistence: S? = null,
)
