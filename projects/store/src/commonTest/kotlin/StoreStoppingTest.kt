package com.javiersc.either.store

import com.javiersc.either.resource.buildResourceSuccess
import com.javiersc.either.resource.buildResourceSuccessLoading
import com.javiersc.either.store.operation.operationsCacheSourceOfTruthRemoteStopping
import com.javiersc.either.store.sourcer.Cacher
import com.javiersc.either.store.sourcer.Fetcher
import com.javiersc.either.store.sourcer.SourceOfTruth
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest

internal class StoreStoppingTest {

    @Test
    fun `Store stopping cache should have one element`() {
        runBlockingTest {
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

            store.stream(2, operationsCacheSourceOfTruthRemoteStopping).take(2).toList() shouldBe
                listOf(
                    buildResourceSuccessLoading(listOf(10, 11)),
                    buildResourceSuccess(listOf(1, 2, 3, 4)),
                )
        }
    }

    @Test
    fun `Store stopping cache should throw job not completed`() {
        shouldThrow<IllegalStateException> {
            runBlockingTest {
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

                store.stream(2, operationsCacheSourceOfTruthRemoteStopping).take(3).toList()
            }
        }
    }
}
