package com.javiersc.either.store.operation

import com.javiersc.either.store.StoreAction
import com.javiersc.either.store.StoreSource

public data class Operation(
    val action: StoreAction,
    val source: StoreSource,
)
