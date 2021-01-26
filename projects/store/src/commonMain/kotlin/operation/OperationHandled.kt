package com.javiersc.either.store.operation

import com.javiersc.either.resource.ResourceEither

public data class OperationHandled<F, S>(
    val cache: S? = null,
    val remote: ResourceEither<F, S>? = null,
    val persistence: S? = null,
)
