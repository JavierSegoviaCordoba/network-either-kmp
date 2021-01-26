package com.javiersc.either.store.sourcer

public interface Cacher<Key : Any, S : Any> {

    public suspend fun get(key: Key): S?

    public suspend fun insert(data: S)

    public suspend fun update(data: S)

    public suspend fun delete(data: S)

    public suspend fun deleteAll()

    public companion object {

        public fun <Key : Any, S : Any> of(
            get: suspend (Key) -> S?,
            insert: suspend (S) -> Unit = {},
            update: suspend (S) -> Unit = {},
            delete: suspend (S) -> Unit = {},
            deleteAll: suspend () -> Unit = {},
        ): Cacher<Key, S> =
            FactoryCacher(
                getFactory = get,
                insertFactory = insert,
                updateFactory = update,
                deleteFactory = delete,
                deleteAllFactory = deleteAll,
            )
    }

    private class FactoryCacher<Key : Any, S : Any>(
        private val getFactory: suspend (Key) -> S?,
        private val insertFactory: suspend (S) -> Unit,
        private val updateFactory: suspend (S) -> Unit,
        private val deleteFactory: suspend (S) -> Unit,
        private val deleteAllFactory: suspend () -> Unit,
    ) : Cacher<Key, S> {

        override suspend fun get(key: Key): S? = getFactory(key)

        override suspend fun insert(data: S) = insertFactory(data)

        override suspend fun update(data: S) = updateFactory(data)

        override suspend fun delete(data: S) = deleteFactory(data)

        override suspend fun deleteAll() = deleteAllFactory()
    }
}
