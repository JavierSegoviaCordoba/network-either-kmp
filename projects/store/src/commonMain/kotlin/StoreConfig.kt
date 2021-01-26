package com.javiersc.either.store

public class StoreConfig {
    public var shouldFetcherOnlyEmitFailure: Boolean = false
    public var shouldFetchIfPersistenceHasData: Boolean = false
    public var shouldFetchIfPersistenceIsEmpty: Boolean = false
}

public fun storeConfig(block: StoreConfig.() -> Unit): StoreConfig = StoreConfig().apply(block)
