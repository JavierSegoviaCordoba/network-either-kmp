@file:Suppress("TooManyFunctions")

package com.javiersc.either.store.operation

import com.javiersc.either.store.StoreAction
import com.javiersc.either.store.StoreSource

public fun operations(block: MutableList<OperationFlow>.() -> Unit): List<OperationFlow> =
    buildList(block)

public fun Operation.emitting(): OperationEmit = OperationEmit(this, emit = EmitAndLoading(), false)

public fun Operation.emittingLoading(): OperationEmit =
    OperationEmit(this, emit = EmitAndLoading(true), false)

public infix fun StoreAction.into(source: StoreSource): Operation = Operation(this, source)

public infix fun StoreAction.from(source: StoreSource): Operation = Operation(this, source)

public infix fun OperationEmit.shouldDo(operation: Operation): OperationFlow =
    OperationFlow(this, listOf(operation))

public infix fun OperationEmit.shouldDo(operations: List<Operation>): OperationFlow =
    OperationFlow(this, operations)

public fun MutableList<OperationFlow>.add(block: () -> OperationEmit) {
    add(OperationFlow(block(), emptyList()))
}

public fun MutableList<OperationFlow>.addAndInvoke(block: () -> OperationFlow) {
    add(block())
}

public fun MutableList<OperationFlow>.addStopping(block: () -> OperationEmit) {
    add(OperationFlow(block().copy(shouldShortCircuit = true), emptyList()))
}

public fun MutableList<OperationFlow>.addAndInvokeStopping(block: () -> OperationFlow) {
    add(block().copy(operationEmit = block().operationEmit.copy(shouldShortCircuit = true)))
}
