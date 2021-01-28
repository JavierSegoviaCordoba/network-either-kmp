package com.javiersc.either.store.sourcer

import com.javiersc.either.store.internal.sourcer.FactorySourceOfTruth
import kotlinx.coroutines.flow.Flow

public interface SourceOfTruth<Key : Any, S : Any> {

    public fun stream(key: Key): Flow<S>

    public suspend fun get(key: Key): S

    public suspend fun insert(data: S)

    public suspend fun update(data: S)

    public suspend fun delete(data: S)

    public suspend fun deleteAll()

    public companion object {

        @Suppress("LongParameterList")
        public fun <Key : Any, S : Any> of(
            stream: (Key) -> Flow<S>,
            get: suspend (Key) -> S,
            insert: suspend (S) -> Unit = {},
            update: suspend (S) -> Unit = {},
            delete: suspend (S) -> Unit = {},
            deleteAll: suspend () -> Unit = {},
        ): SourceOfTruth<Key, S> =
            FactorySourceOfTruth(
                streamFactory = stream,
                getFactory = get,
                insertFactory = insert,
                updateFactory = update,
                deleteFactory = delete,
                deleteAllFactory = deleteAll,
            )
    }
}
