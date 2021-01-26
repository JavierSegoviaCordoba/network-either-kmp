package com.javiersc.either.store

import com.javiersc.either.resource.buildResourceSuccess
import com.javiersc.either.resource.buildResourceSuccessLoading
import com.javiersc.either.store.sourcer.Cacher
import com.javiersc.either.store.sourcer.Fetcher
import com.javiersc.either.store.sourcer.SourceOfTruth
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest

internal class StoreTakeTest {

    @Test
    fun `Store test`() {
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

            store.stream(2).take(4).toList() shouldBe
                listOf(
                    buildResourceSuccessLoading(listOf(10, 11)),
                    buildResourceSuccessLoading(listOf(1, 2, 3, 4)),
                    buildResourceSuccess(listOf(7, 8, 2)),
                    buildResourceSuccess(listOf(1, 2, 3, 4, 7, 8, 2)),
                )
        }
    }
}
