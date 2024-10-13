package com.javiersc.network.either.internal.deferred

import com.javiersc.network.either.NetworkEither
import java.lang.reflect.Type
import kotlinx.coroutines.CompletableDeferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter

internal class NetworkEitherDeferredCallAdapter<F : Any, S : Any>(
    private val successBodyType: Type,
    private val errorConverter: Converter<ResponseBody, F>,
    private val isNetworkAvailable: suspend () -> Boolean,
) : CallAdapter<S, CompletableDeferred<NetworkEither<F, S>>> {

    override fun responseType(): Type = successBodyType

    override fun adapt(call: Call<S>): CompletableDeferred<NetworkEither<F, S>> =
        deferredAdapt(call, errorConverter, isNetworkAvailable)
}
