package com.javiersc.either.store.operation

public data class OperationEmit(
    val operation: Operation,
    val emit: EmitAndLoading?,
    val shouldShortCircuit: Boolean,
)

public data class EmitAndLoading(val shouldLoad: Boolean = false)
