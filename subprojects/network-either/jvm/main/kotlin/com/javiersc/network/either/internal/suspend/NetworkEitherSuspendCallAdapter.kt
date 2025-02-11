package com.javiersc.network.either.internal.suspend

import com.javiersc.network.either.NetworkEither
import java.lang.reflect.Type
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter

internal class NetworkEitherSuspendCallAdapter<F : Any, S : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, F>,
    private val isNetworkAvailable: suspend () -> Boolean,
) : CallAdapter<S, Call<NetworkEither<F, S>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkEither<F, S>> {
        return NetworkEitherSuspendCall(call, errorBodyConverter, isNetworkAvailable)
    }
}
