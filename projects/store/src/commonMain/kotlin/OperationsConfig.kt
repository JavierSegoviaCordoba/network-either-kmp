package com.javiersc.either.store

import com.javiersc.either.store.operation.OperationFlow
import com.javiersc.either.store.operation.operationsCacheSourceOfTruthRemote

public class OperationsConfig(
    public var shouldStopWithEmptyList: Boolean = false,
    public var operations: List<OperationFlow> = operationsCacheSourceOfTruthRemote,
)

public fun operationsConfig(block: OperationsConfig.() -> Unit): OperationsConfig =
    OperationsConfig().apply(block)

public fun operations(operations: List<OperationFlow>): OperationsConfig =
    OperationsConfig().apply { this.operations = operations }

public fun operations(block: MutableList<OperationFlow>.() -> Unit): OperationsConfig =
    OperationsConfig().apply { operations = buildList(block) }
