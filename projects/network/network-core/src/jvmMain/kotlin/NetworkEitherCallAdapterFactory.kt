package com.javiersc.either.network

import com.javiersc.either.Either
import com.javiersc.either.network.internal.deferred.NetworkEitherDeferredCallAdapter
import com.javiersc.either.network.internal.suspend.NetworkEitherSuspendCallAdapter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import com.javiersc.either.network.utils.isNetworkAvailable as internalIsNetworkAvailable

public class NetworkEitherCallAdapterFactory(
    private val isNetworkAvailable: () -> Boolean = { internalIsNetworkAvailable }
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        check(returnType is ParameterizedType) { "$returnType must be parameterized. Raw types are not supported" }

        val containerType = getParameterUpperBound(0, returnType)
        if (getRawType(containerType) != Either::class.java) return null

        check(containerType is ParameterizedType) {
            "$containerType must be parameterized. Raw types are not supported"
        }

        val (errorBodyType, successBodyType) = containerType.getBodyTypes()
        val errorBodyConverter = retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return when (getRawType(returnType)) {
            Deferred::class.java ->
                NetworkEitherDeferredCallAdapter<Any, Any>(successBodyType, errorBodyConverter, isNetworkAvailable)
            Call::class.java ->
                NetworkEitherSuspendCallAdapter<Any, Any>(successBodyType, errorBodyConverter, isNetworkAvailable)
            else -> null
        }
    }

    private fun ParameterizedType.getBodyTypes(): Pair<Type, Type> {
        val failureWrapperType: ParameterizedType = getParameterUpperBound(0, this) as ParameterizedType
        val errorType = getParameterUpperBound(0, failureWrapperType)
        val successWrapperType: ParameterizedType = getParameterUpperBound(1, this) as ParameterizedType
        val successType = getParameterUpperBound(0, successWrapperType)
        return errorType to successType
    }
}
