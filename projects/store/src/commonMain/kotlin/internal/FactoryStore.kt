package com.javiersc.either.store.internal

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.ResourceSuccess
import com.javiersc.either.resource.buildResourceSuccess
import com.javiersc.either.resource.buildResourceSuccessLoading
import com.javiersc.either.resource.extensions.successDataOrNull
import com.javiersc.either.store.OperationsConfig
import com.javiersc.either.store.Store
import com.javiersc.either.store.StoreAction
import com.javiersc.either.store.StoreException
import com.javiersc.either.store.StoreSource
import com.javiersc.either.store.internal.operation.handleOperation
import com.javiersc.either.store.operation.Operation
import com.javiersc.either.store.operation.OperationFlow
import com.javiersc.either.store.sourcer.Cacher
import com.javiersc.either.store.sourcer.Fetcher
import com.javiersc.either.store.sourcer.SourceOfTruth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class FactoryStore<Key : Any, F : Any, S : Any>(
    private val cacher: Cacher<Key, S>?,
    private val fetcher: Fetcher<Key, F, S>?,
    private val sourceOfTruth: SourceOfTruth<Key, S>?,
) : Store<Key, F, S> {

    override fun stream(
        key: Key,
        config: OperationsConfig,
    ): Flow<ResourceEither<F, S>> =
        channelFlow {
            run {
                config.operations.onEach { operationFlow ->
                    val sourceFlow = operationFlow.operationEmit.operation.source
                    val actionFlow = operationFlow.operationEmit.operation.action

                    if (isFetching(key, sourceFlow, actionFlow)) return@onEach

                    val (
                        cacheData: S?,
                        remoteResource: ResourceEither<F, S>?,
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

                    fetched(key, actionFlow)

                    if (shouldStop(cacheData, remoteResource, sotData, config, operationFlow)) {
                        return@run
                    }

                    operationFlow.operations.onEach deepOnEach@{ operation ->
                        val source = operation.source
                        val action = operation.action

                        if (isFetching(key, source, action)) return@deepOnEach

                        handleOperation(
                            key = key,
                            data = cacheData ?: sotData,
                            resource = remoteResource,
                            operationEmit = null,
                            operation = operation,
                            cacher = cacher,
                            fetcher = fetcher,
                            sot = sourceOfTruth,
                            flow = this@channelFlow,
                        )

                        fetched(key, action)
                    }
                }
            }

            launch {
                sourceOfTruth?.stream(key)?.collect { data: S ->
                    send(buildResourceSuccess<F, S>(data))
                }
            }
        }

    override suspend fun get(key: Key, operations: List<Operation>): ResourceEither<F, S> {
        try {
            getFromCache(key)?.let { data: S ->
                return buildResourceSuccessLoading(data)
            }
        } catch (exception: StoreException) {
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
        } catch (exception: StoreException) {
            println(exception.message)
        }

        try {
            return getFromRemote(key, operations)
        } catch (exception: StoreException) {
            println(exception.message)
        } catch (exception: StoreException) {
            println(exception.message)
        }

        error("Store is not configured correctly")
    }

    override suspend fun getFromCache(key: Key): S? {
        return requireNotNull(cacher).get(key)
    }

    override suspend fun getFromRemote(
        key: Key,
        operations: List<Operation>,
    ): ResourceEither<F, S> {

        // TODO: instead of throw an exception, wait and get what is fetching now
        if (isFetching(key, StoreSource.Remote, StoreAction.Get)) {
            storeError("Fetcher is currently fetching data")
        }

        return requireNotNull(fetcher).get(key).also { resource ->
            fetched(key, StoreAction.Get)

            operations.forEach { operation ->
                handleOperation(
                    key = key,
                    data = null,
                    resource = resource,
                    operationEmit = null,
                    operation = operation,
                    cacher = cacher,
                    fetcher = fetcher,
                    sot = sourceOfTruth,
                    flow = null,
                )
            }
        }
    }

    override suspend fun getFromSourceOfTruth(key: Key): S = requireNotNull(sourceOfTruth).get(key)

    override suspend fun clear(data: S) {
        requireNotNull(sourceOfTruth).delete(data)
    }

    override suspend fun clearAll() {
        requireNotNull(sourceOfTruth).deleteAll()
    }

    private val isFetching: MutableMap<Pair<Key, StoreAction>, Boolean> = mutableMapOf()

    private fun isFetching(key: Key, source: StoreSource, action: StoreAction): Boolean =
        when (source) {
            StoreSource.Remote -> {
                if (isFetching[Pair(key, action)] == true) true
                else {
                    isFetching[Pair(key, action)] = true
                    false
                }
            }
            else -> false
        }

    private fun fetched(key: Key, action: StoreAction) {
        isFetching.remove(Pair(key, action))
    }

    private fun shouldStop(
        cacheData: S?,
        remoteResource: ResourceEither<F, S>?,
        sotData: S?,
        operationsConfig: OperationsConfig,
        operationFlow: OperationFlow
    ): Boolean {
        val hasData = cacheData != null || remoteResource is ResourceSuccess<S> || sotData != null

        val cacheDataIsEmptyList = (cacheData is List<*>) && cacheData.isEmpty()
        val remoteData = remoteResource?.successDataOrNull()
        val remoteDataIsEmptyList = (remoteData is List<*>) && (remoteData.isEmpty())
        val sotDataIsEmptyList = (sotData is List<*>) && sotData.isEmpty()

        val shouldStopWithEmptyList = (operationsConfig.shouldStopWithEmptyList)

        val dataIsEmptyList = (cacheDataIsEmptyList || remoteDataIsEmptyList || sotDataIsEmptyList)

        val shouldShortCircuit = operationFlow.operationEmit.shouldShortCircuit

        return (if (shouldStopWithEmptyList) hasData && dataIsEmptyList else hasData) &&
            shouldShortCircuit
    }
}
