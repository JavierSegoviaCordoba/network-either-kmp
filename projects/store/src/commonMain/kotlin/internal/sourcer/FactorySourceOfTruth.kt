package com.javiersc.either.store.internal.sourcer

import com.javiersc.either.store.sourcer.SourceOfTruth
import kotlinx.coroutines.flow.Flow

internal class FactorySourceOfTruth<Key : Any, S : Any>(
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
