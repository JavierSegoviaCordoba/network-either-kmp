package com.javiersc.network.either

import com.javiersc.network.either.internal.deferred.NetworkEitherDeferredCallAdapter
import com.javiersc.network.either.internal.suspend.NetworkEitherSuspendCallAdapter
import com.javiersc.network.either.utils.isNetworkAvailable as internalIsNetworkAvailable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit

public class NetworkEitherCallAdapterFactory(
    private val isNetworkAvailable: () -> Boolean = { internalIsNetworkAvailable }
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        check(returnType is ParameterizedType) {
            "$returnType must be parameterized. Raw types are not supported"
        }

        val containerType = getParameterUpperBound(0, returnType)
        if (!containerType.isNetworkEitherType) return null

        check(containerType is ParameterizedType) {
            "$containerType must be parameterized. Raw types are not supported"
        }

        val (errorBodyType, successBodyType) = containerType.getBodyTypes()
        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return when (getRawType(returnType)) {
            Deferred::class.java ->
                NetworkEitherDeferredCallAdapter<Any, Any>(
                    successBodyType, errorBodyConverter, isNetworkAvailable)
            Call::class.java ->
                NetworkEitherSuspendCallAdapter<Any, Any>(
                    successBodyType, errorBodyConverter, isNetworkAvailable)
            else -> null
        }
    }

    private val Type.isNetworkEitherType: Boolean
        get() =
            getRawType(this) != NetworkEither.Failure::class.java ||
                getRawType(this) != NetworkEither.Failure.Http::class.java ||
                getRawType(this) != NetworkEither.Failure.Local::class.java ||
                getRawType(this) != NetworkEither.Failure.Remote::class.java ||
                getRawType(this) != NetworkEither.Failure.Unknown::class.java ||
                getRawType(this) != NetworkEither.Success::class.java

    private fun ParameterizedType.getBodyTypes(): Pair<Type, Type> {
        val errorType: Type = getParameterUpperBound(0, this)
        val successType: Type = getParameterUpperBound(1, this)
        return errorType to successType
    }
}
