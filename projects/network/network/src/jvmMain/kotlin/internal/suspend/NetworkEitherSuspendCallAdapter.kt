package com.javiersc.either.network.internal.suspend

import com.javiersc.either.network.NetworkEither
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

internal class NetworkEitherSuspendCallAdapter<F : Any, S : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, F>,
    private val isNetworkAvailable: () -> Boolean,
) : CallAdapter<S, Call<NetworkEither<F, S>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkEither<F, S>> {
        return NetworkEitherSuspendCall(call, errorBodyConverter, isNetworkAvailable)
    }
}
