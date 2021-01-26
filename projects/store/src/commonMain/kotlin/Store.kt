@file:Suppress("LongParameterList")

package com.javiersc.either.store

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.ResourceSuccess
import com.javiersc.either.resource.buildResourceSuccess
import com.javiersc.either.resource.buildResourceSuccessLoading
import com.javiersc.either.store.operation.OperationFlow
import com.javiersc.either.store.operation.handleOperation
import com.javiersc.either.store.operation.operationsCacheSourceOfTruthRemote
import com.javiersc.either.store.sourcer.Cacher
import com.javiersc.either.store.sourcer.Fetcher
import com.javiersc.either.store.sourcer.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

public interface Store<Key : Any, F : Any, S : Any> {

    public fun stream(
        key: Key,
        operations: List<OperationFlow> = operationsCacheSourceOfTruthRemote,
    ): Flow<ResourceEither<F, S>>

    public suspend fun getFromCache(key: Key): S?

    public suspend fun fetch(key: Key): ResourceEither<F, S>

    public suspend fun getFromSourceOfTruth(key: Key): S

    public suspend fun get(key: Key): ResourceEither<F, S>

    public suspend fun clear(data: S)

    public suspend fun clearAll()

    public companion object {

        public operator fun <Key : Any, F : Any, S : Any> invoke(
            cacher: Cacher<Key, S>? = null,
            fetcher: Fetcher<Key, F, S>? = null,
            sourceOfTruth: SourceOfTruth<Key, S>? = null,
        ): Store<Key, F, S> = FactoryStore(cacher, fetcher, sourceOfTruth)
    }

    private class FactoryStore<Key : Any, F : Any, S : Any>(
        private val cacher: Cacher<Key, S>?,
        private val fetcher: Fetcher<Key, F, S>?,
        private val sourceOfTruth: SourceOfTruth<Key, S>?,
    ) : Store<Key, F, S> {

        override fun stream(
            key: Key,
            operations: List<OperationFlow>,
        ): Flow<ResourceEither<F, S>> =
            channelFlow {
                run {
                    operations.onEach { operationFlow ->
                        val (
                            cacheData: S?,
                            remoteData: ResourceEither<F, S>?,
                            sotData: S?,
                        ) = handleOperation(
                            key = key,
                            data = null,
                            resource = null,
                            operationEmit = operationFlow.operationEmit,
                            operation = null,
                            cacher = cacher,
                            fetcher = fetcher,
                            sot = sourceOfTruth,
                            flow = this@channelFlow,
                        )

                        val hasData =
                            cacheData != null || remoteData is ResourceSuccess<S> || sotData != null

                        val shouldShortCircuit =
                            hasData && operationFlow.operationEmit.shouldShortCircuit

                        val shouldStop = hasData && shouldShortCircuit

                        if (shouldStop) return@run

                        operationFlow.operations.onEach { operation ->
                            handleOperation(
                                key = key,
                                data = cacheData ?: sotData,
                                resource = remoteData,
                                operationEmit = null,
                                operation = operation,
                                cacher = cacher,
                                fetcher = fetcher,
                                sot = sourceOfTruth,
                                flow = this@channelFlow,
                            )
                        }
                    }
                }

                launch {
                    sourceOfTruth?.stream(key)?.collect { data: S ->
                        send(buildResourceSuccess<F, S>(data))
                    }
                }
            }

        override suspend fun getFromCache(key: Key): S? {
            return requireNotNull(cacher).get(key)
        }

        override suspend fun fetch(key: Key): ResourceEither<F, S> {

            if (isFetching[key] == true) {
                throw IllegalStateException("Fetcher is currently fetching data")
            }

            isFetching[key] = true
            return requireNotNull(fetcher).get(key).also { resource ->
                isFetching[key] = false

                if (resource is ResourceSuccess<S>) {
                    cacher?.insert(resource.right.data)
                    sourceOfTruth?.insert(resource.right.data)
                }
            }
        }

        override suspend fun getFromSourceOfTruth(key: Key): S =
            requireNotNull(sourceOfTruth).get(key)

        override suspend fun get(key: Key): ResourceEither<F, S> {
            try {
                getFromCache(key)?.let { data: S ->
                    return buildResourceSuccessLoading(data)
                }
            } catch (exception: IllegalArgumentException) {
                println(exception.message)
            }

            try {
                val data = getFromSourceOfTruth(key)

                // TODO: Check this
                // if (!config.shouldFetchIfPersistenceIsEmpty ||
                //     data !is List<*> ||
                //     data.isNotEmpty()) {
                //     return buildResourceSuccessLoading(data)
                // }
            } catch (exception: IllegalArgumentException) {
                println(exception.message)
            }

            try {
                return fetch(key)
            } catch (exception: IllegalArgumentException) {
                println(exception.message)
            } catch (exception: IllegalStateException) {
                println(exception.message)
            }

            error("Store is not configured correctly")
        }

        override suspend fun clear(data: S) {
            requireNotNull(sourceOfTruth).delete(data)
        }

        override suspend fun clearAll() {
            requireNotNull(sourceOfTruth).deleteAll()
        }

        private val isFetching: MutableMap<Key, Boolean> = mutableMapOf()
    }
}
