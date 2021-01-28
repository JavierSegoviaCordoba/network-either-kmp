@file:Suppress("LongParameterList")

package com.javiersc.either.store

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.store.internal.FactoryStore
import com.javiersc.either.store.operation.Operation
import com.javiersc.either.store.sourcer.Cacher
import com.javiersc.either.store.sourcer.Fetcher
import com.javiersc.either.store.sourcer.SourceOfTruth
import kotlinx.coroutines.flow.Flow

public interface Store<Key : Any, F : Any, S : Any> {

    public fun stream(
        key: Key,
        config: OperationsConfig = OperationsConfig(),
    ): Flow<ResourceEither<F, S>>

    public suspend fun get(key: Key, operations: List<Operation>): ResourceEither<F, S>

    public suspend fun getFromCache(key: Key): S?

    public suspend fun getFromRemote(
        key: Key,
        operations: List<Operation> = emptyList(),
    ): ResourceEither<F, S>

    public suspend fun getFromSourceOfTruth(key: Key): S

    public suspend fun clear(data: S)

    public suspend fun clearAll()

    public companion object {

        public operator fun <Key : Any, F : Any, S : Any> invoke(
            cacher: Cacher<Key, S>? = null,
            fetcher: Fetcher<Key, F, S>? = null,
            sourceOfTruth: SourceOfTruth<Key, S>? = null,
        ): Store<Key, F, S> = FactoryStore(cacher, fetcher, sourceOfTruth)
    }
}
