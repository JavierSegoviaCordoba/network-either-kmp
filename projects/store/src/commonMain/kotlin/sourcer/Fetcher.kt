package com.javiersc.either.store.sourcer

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.store.internal.sourcer.FactoryFetcher

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
}
