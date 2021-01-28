@file:Suppress("LongParameterList", "ComplexMethod", "LongMethod", "NestedBlockDepth")

package com.javiersc.either.store.internal.operation

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.ResourceFailure
import com.javiersc.either.resource.ResourceSuccess
import com.javiersc.either.resource.buildResourceFailure
import com.javiersc.either.resource.buildResourceSuccess
import com.javiersc.either.store.StoreAction
import com.javiersc.either.store.StoreSource
import com.javiersc.either.store.internal.storeError
import com.javiersc.either.store.operation.Operation
import com.javiersc.either.store.operation.OperationEmit
import com.javiersc.either.store.sourcer.Cacher
import com.javiersc.either.store.sourcer.Fetcher
import com.javiersc.either.store.sourcer.SourceOfTruth
import kotlinx.coroutines.channels.ProducerScope

internal suspend fun <Key : Any, F : Any, S : Any> handleOperation(
    key: Key,
    data: S?,
    resource: ResourceEither<F, S>?,
    operationEmit: OperationEmit?,
    operation: Operation?,
    cacher: Cacher<Key, S>?,
    fetcher: Fetcher<Key, F, S>?,
    sot: SourceOfTruth<Key, S>?,
    flow: ProducerScope<ResourceEither<F, S>>?,
): OperationHandled<F, S> {
    val internalOperation =
        operationEmit?.operation ?: operation ?: storeError("There is no operations")
    val action = internalOperation.action
    val source = internalOperation.source
    val shouldEmit = operationEmit?.emit != null
    val shouldLoad = operationEmit?.emit?.shouldLoad ?: false

    var cache: S? = null
    var remote: ResourceEither<F, S>? = null
    var persistence: S? = null

    when (action) {
        StoreAction.Get -> {
            when (source) {
                StoreSource.Cache -> {
                    cache = cacher?.get(key)
                    if (cache != null && shouldEmit) {
                        flow?.send(buildResourceSuccess(cache, shouldLoad))
                    }
                }
                StoreSource.Remote -> {
                    remote = fetcher?.get(key)
                    if (shouldEmit) {
                        when (remote) {
                            is ResourceFailure<F, S> ->
                                flow?.send(
                                    buildResourceFailure(
                                        remote.left.failure,
                                        remote.left.data,
                                        shouldLoad,
                                    ),
                                )
                            is ResourceSuccess<S> ->
                                flow?.send(buildResourceSuccess(remote.right.data, shouldLoad))
                        }
                    }
                }
                StoreSource.SourceOfTruth -> {
                    persistence = sot?.get(key)
                    if (shouldEmit && persistence != null) {
                        flow?.send(buildResourceSuccess(persistence, shouldLoad))
                    }
                }
            }
        }
        StoreAction.Insert -> {
            when (source) {
                StoreSource.Cache -> {
                    if (data != null) cacher?.insert(data)
                    if (resource is ResourceSuccess<S>) cacher?.insert(resource.right.data)

                    if (shouldEmit && sot != null) {
                        flow?.send(buildResourceSuccess(sot.get(key), shouldLoad))
                    }
                }
                StoreSource.Remote -> TODO()
                StoreSource.SourceOfTruth -> {
                    if (data != null) sot?.insert(data)
                    if (resource is ResourceSuccess<S>) sot?.insert(resource.right.data)

                    if (shouldEmit && sot != null) {
                        flow?.send(buildResourceSuccess(sot.get(key), shouldLoad))
                    }
                }
            }
        }
        StoreAction.Update -> {
            when (source) {
                StoreSource.Cache -> {
                    if (data != null) cacher?.update(data)
                    if (resource is ResourceSuccess<S>) cacher?.update(resource.right.data)

                    if (shouldEmit && sot != null) {
                        flow?.send(buildResourceSuccess(sot.get(key), shouldLoad))
                    }
                }
                StoreSource.Remote -> TODO()
                StoreSource.SourceOfTruth -> {
                    if (data != null) sot?.update(data)
                    if (resource is ResourceSuccess<S>) sot?.update(resource.right.data)

                    if (shouldEmit && sot != null) {
                        flow?.send(buildResourceSuccess(sot.get(key), shouldLoad))
                    }
                }
            }
        }
        StoreAction.Delete -> {
            when (source) {
                StoreSource.Cache -> {
                    if (data != null) cacher?.delete(data)
                    if (resource is ResourceSuccess<S>) cacher?.delete(resource.right.data)

                    if (shouldEmit && sot != null) {
                        flow?.send(buildResourceSuccess(sot.get(key), shouldLoad))
                    }
                }
                StoreSource.Remote -> TODO()
                StoreSource.SourceOfTruth -> {
                    if (data != null) sot?.delete(data)
                    if (resource is ResourceSuccess<S>) sot?.delete(resource.right.data)

                    if (shouldEmit && sot != null) {
                        flow?.send(buildResourceSuccess(sot.get(key), shouldLoad))
                    }
                }
            }
        }
        StoreAction.DeleteAll -> {
            when (source) {
                StoreSource.Cache -> {
                    cacher?.deleteAll()

                    if (shouldEmit && sot != null) {
                        flow?.send(buildResourceSuccess(sot.get(key), shouldLoad))
                    }
                }
                StoreSource.Remote -> TODO()
                StoreSource.SourceOfTruth -> {
                    sot?.deleteAll()

                    if (shouldEmit && sot != null) {
                        flow?.send(buildResourceSuccess(sot.get(key), shouldLoad))
                    }
                }
            }
        }
    }

    return OperationHandled(cache, remote, persistence)
}
