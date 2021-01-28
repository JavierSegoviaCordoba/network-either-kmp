@file:Suppress("MagicNumber")

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.buildResourceSuccess
import com.javiersc.either.store.Store
import com.javiersc.either.store.sourcer.Cacher
import com.javiersc.either.store.sourcer.Fetcher
import com.javiersc.either.store.sourcer.SourceOfTruth
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

internal fun main() {
    runBlocking {
        val now = Instant.now()

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

        store.stream(2).collect {
            println(it)
            println("Duration: ${Duration.between(now, Instant.now()).toMillis()}")
        }
    }
}

internal class CacheDataSource {

    private var _numbers: MutableList<Int>? = mutableListOf(10, 11)

    suspend fun get(key: Int): List<Int>? {
        delay(1500).also { println("GET CACHE") }
        return _numbers?.toList()
    }

    suspend fun insert(numbers: List<Int>) {
        delay(1500).also { println("INSERT CACHE: $numbers") }
        if (_numbers != null) _numbers?.addAll(numbers) else _numbers = numbers.toMutableList()
    }

    suspend fun delete(numbers: List<Number>) {
        delay(1500).also { println("DELETE CACHE: $numbers") }
        _numbers?.removeAll(numbers)
    }

    suspend fun deleteAll() {
        delay(1500).also { println("DELETE_ALL CACHE") }
        _numbers?.clear()
    }
}

internal class RemoteDataSource {

    suspend fun get(key: Int): ResourceEither<Boolean, List<Int>> {
        delay(1500).also { println("GET REMOTE") }
        return buildResourceSuccess(listOf(7, 8, key))
    }
}

internal class LocalDataSource {

    private val _numbers: MutableList<Int> = mutableListOf(1, 2, 3, 4)

    private val _flow: MutableStateFlow<List<Int>> = MutableStateFlow(_numbers.toList())

    fun stream(key: Int): Flow<List<Int>> = _flow

    suspend fun get(key: Int): List<Int> {
        delay(1500).also { println("GET SOT") }
        return _numbers.toList()
    }

    suspend fun write(numbers: List<Int>) =
        doAndEmit {
            delay(1500).also { println("INSERT SOT: $numbers") }
            _numbers.addAll(numbers)
        }

    suspend fun delete(numbers: List<Number>) =
        doAndEmit {
            delay(1500).also { println("DELETE SOT: $numbers") }
            _numbers.removeAll(numbers)
        }

    suspend fun deleteAll() =
        doAndEmit {
            delay(1500).also { println("DELETE_ALL SOT") }
            _numbers.clear()
        }

    private suspend fun doAndEmit(block: suspend () -> Unit) =
        block().also {
            val numbers = _numbers.toList()
            _flow.emit(numbers)
        }
}
