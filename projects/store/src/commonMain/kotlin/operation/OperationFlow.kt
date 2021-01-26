package com.javiersc.either.store.operation

public data class OperationFlow(
    val operationEmit: OperationEmit,
    val operations: List<Operation>,
)
