package com.javiersc.either.store.internal.sourcer

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.store.sourcer.Fetcher

internal class FactoryFetcher<Key : Any, F : Any, S : Any>(
    private val factoryGet: suspend (Key) -> ResourceEither<F, S>,
) : Fetcher<Key, F, S> {

    override suspend fun get(key: Key): ResourceEither<F, S> = factoryGet(key)
}
