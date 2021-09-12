package com.javiersc.either.network.retrofit.config

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkEitherCallAdapterFactory
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET

internal interface DogService {

    @GET("dos") fun getDogAsync(): Deferred<NetworkEither<ErrorDTO, DogDTO>>

    @GET("dog") suspend fun getDog(): NetworkEither<ErrorDTO, DogDTO>

    @GET("dog") fun getDogWithoutBodyAsync(): Deferred<NetworkEither<ErrorDTO, Unit>>

    @GET("dog") suspend fun getDogWithoutBody(): NetworkEither<ErrorDTO, Unit>

    companion object {

        fun getService(httpUrl: HttpUrl, timeoutMillis: Long = 200): DogService =
            Retrofit.Builder()
                .apply {
                    baseUrl(httpUrl)
                    client(okHttpClient(timeoutMillis))
                    addCallAdapterFactory(NetworkEitherCallAdapterFactory())
                    addConverterFactory(converter)
                }
                .build()
                .create()

        fun getServicefailureLocal(httpUrl: HttpUrl, timeoutMillis: Long = 200): DogService =
            Retrofit.Builder()
                .apply {
                    baseUrl(httpUrl)
                    client(okHttpClient(timeoutMillis))
                    addCallAdapterFactory(NetworkEitherCallAdapterFactory { false })
                    addConverterFactory(converter)
                }
                .build()
                .create()

        private val converter =
            Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
                .asConverterFactory("application/json".toMediaType())

        private fun okHttpClient(timeoutMillis: Long) =
            OkHttpClient.Builder()
                .apply {
                    callTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                    connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                }
                .build()
    }
}
