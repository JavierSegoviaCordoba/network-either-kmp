package com.javiersc.network.either.retrofit.config

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEitherCallAdapterFactory
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
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
                    addCallAdapterFactory(
                        NetworkEitherCallAdapterFactory(isNetworkAvailable = { false })
                    )
                    addConverterFactory(converter)
                }
                .build()
                .create()

        private val defaultJson = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        private val converter = defaultJson.asConverterFactory("application/json".toMediaType())

        private fun okHttpClient(timeoutMillis: Long) =
            OkHttpClient.Builder()
                .apply {
                    callTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                    connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                }
                .build()
    }
}
