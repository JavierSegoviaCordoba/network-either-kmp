package com.javiersc.either.store

import app.cash.turbine.test
import com.javiersc.either.resource.buildResourceSuccess
import com.javiersc.either.resource.buildResourceSuccessLoading
import com.javiersc.either.store.sourcer.Cacher
import com.javiersc.either.store.sourcer.Fetcher
import com.javiersc.either.store.sourcer.SourceOfTruth
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.time.seconds

internal class StoreTurbineTest {

    @Test
    fun `Store test`() {
        suspendTest {
            val cacheDataSource = CacheDataSource()
            val remoteDataSource = RemoteDataSource()
            val localDataSource = LocalDataSource()

            val store: Store<Int, Boolean, List<Int>> =
                Store(
                    cacher =
                    Cacher.of(
                        get = cacheDataSource::get,
                        insert = cacheDataSource::insert,
                            delete = cacheDataSource::delete,
                            deleteAll = cacheDataSource::deleteAll,
                        ),
                    fetcher =
                        Fetcher.of(
                            get = remoteDataSource::get,
                        ),
                    sourceOfTruth =
                        SourceOfTruth.of(
                            stream = localDataSource::stream,
                            get = localDataSource::get,
                            insert = localDataSource::write,
                            delete = localDataSource::delete,
                            deleteAll = localDataSource::deleteAll,
                        ),
                )

            store.stream(2).test(timeout = 100.seconds) {
                expectItem() shouldBe buildResourceSuccessLoading(listOf(10, 11))
                expectItem() shouldBe buildResourceSuccessLoading(listOf(1, 2, 3, 4))
                expectItem() shouldBe buildResourceSuccess(listOf(7, 8, 2))
                expectItem() shouldBe buildResourceSuccess(listOf(1, 2, 3, 4, 7, 8, 2))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
