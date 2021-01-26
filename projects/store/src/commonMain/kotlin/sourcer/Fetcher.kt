package com.javiersc.either.store.sourcer

import com.javiersc.either.resource.ResourceEither

public interface Fetcher<Key : Any, F : Any, S : Any> {

    public suspend fun get(key: Key): ResourceEither<F, S>

    public companion object {

        public fun <Key : Any, F : Any, S : Any> of(
            get: suspend (Key) -> ResourceEither<F, S>,
        ): Fetcher<Key, F, S> =
            FactoryFetcher(
                factoryGet = get,
            )
    }

    private class FactoryFetcher<Key : Any, F : Any, S : Any>(
        private val factoryGet: suspend (Key) -> ResourceEither<F, S>,
    ) : Fetcher<Key, F, S> {

        override suspend fun get(key: Key): ResourceEither<F, S> = factoryGet(key)
    }
}
