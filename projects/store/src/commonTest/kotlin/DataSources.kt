package com.javiersc.either.store

import com.javiersc.either.resource.ResourceEither
import com.javiersc.either.resource.buildResourceSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class CacheDataSource {

    private var _numbers: MutableList<Int>? = mutableListOf(10, 11)

    suspend fun get(key: Int): List<Int>? {
        delay(200).also { println("GET CACHE") }
        return _numbers?.toList()
    }

    suspend fun insert(numbers: List<Int>) {
        delay(200).also { println("INSERT CACHE: $numbers") }
        if (_numbers != null) _numbers?.addAll(numbers)
        else _numbers = numbers.toMutableList()
    }

    suspend fun delete(numbers: List<Number>) {
        delay(200).also { println("DELETE CACHE: $numbers") }
        _numbers?.removeAll(numbers)
        if (_numbers.isNullOrEmpty()) _numbers = null
    }

    suspend fun deleteAll() {
        delay(200).also { println("DELETE_ALL CACHE") }
        _numbers = null
    }
}

internal class RemoteDataSource {
    suspend fun get(key: Int): ResourceEither<Boolean, List<Int>> {
        delay(600).also { println("GET REMOTE") }
        return buildResourceSuccess(listOf(7, 8, key))
    }
}

internal class LocalDataSource {

    private val _numbers: MutableList<Int> = mutableListOf(1, 2, 3, 4)

    private val _flow: MutableStateFlow<List<Int>> = MutableStateFlow(_numbers.toList())

    fun stream(key: Int): Flow<List<Int>> = _flow

    suspend fun get(key: Int): List<Int> {
//        delay(1500).also { println("GET SOT") }
        return _numbers.toList()
    }

    suspend fun write(numbers: List<Int>) =
        doAndEmit {
            delay(200).also { println("INSERT SOT: $numbers") }
            _numbers.addAll(numbers)
        }

    suspend fun delete(numbers: List<Number>) =
        doAndEmit {
            delay(200).also { println("DELETE SOT: $numbers") }
            _numbers.removeAll(numbers)
        }

    suspend fun deleteAll() =
        doAndEmit {
            delay(200).also { println("DELETE_ALL SOT") }
            _numbers.clear()
        }

    private suspend fun doAndEmit(block: suspend () -> Unit) =
        block().also {
            val numbers = _numbers.toList()
            _flow.emit(numbers)
        }
}
