package com.javiersc.either.store.sourcer

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

    private class FactorySourceOfTruth<Key : Any, S : Any>(
        private val streamFactory: (Key) -> Flow<S>,
        private val getFactory: suspend (Key) -> S,
        private val insertFactory: suspend (S) -> Unit,
        private val updateFactory: suspend (S) -> Unit,
        private val deleteFactory: suspend (S) -> Unit,
        private val deleteAllFactory: suspend () -> Unit,
    ) : SourceOfTruth<Key, S> {

        override fun stream(key: Key): Flow<S> = streamFactory(key)

        override suspend fun get(key: Key): S = getFactory(key)

        override suspend fun insert(data: S) = insertFactory(data)

        override suspend fun update(data: S) = updateFactory(data)

        override suspend fun delete(data: S) = deleteFactory(data)

        override suspend fun deleteAll() = deleteAllFactory()
    }
}
