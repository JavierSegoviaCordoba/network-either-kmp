package com.javiersc.either.store.sourcer

import com.javiersc.either.store.internal.sourcer.FactoryCacher

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
}
