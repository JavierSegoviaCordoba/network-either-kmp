package com.javiersc.network.either._config

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEitherCallAdapterFactory
import com.javiersc.network.either._config.models.DogDTO
import com.javiersc.network.either._config.models.ErrorDTO
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

internal interface DogService {

    @GET("dog/{testNumber}")
    suspend fun getDog(
        @Path("testNumber") testNumber: Int,
    ): NetworkEither<ErrorDTO, DogDTO>

    @GET("dog/{testNumber}")
    fun getDogAsync(
        @Path("testNumber") testNumber: Int,
    ): Deferred<NetworkEither<ErrorDTO, DogDTO>>

    @GET("dog/{testNumber}")
    suspend fun getDogWithoutBody(
        @Path("testNumber") testNumber: Int,
    ): NetworkEither<ErrorDTO, Unit>

    @GET("dog/{testNumber}")
    fun getDogWithoutBodyAsync(
        @Path("testNumber") testNumber: Int,
    ): Deferred<NetworkEither<ErrorDTO, Unit>>

    companion object {

        fun getService(
            port: Int,
            isNetworkAvailable: Boolean = true,
            timeoutMillis: Long = 200
        ): DogService =
            Retrofit.Builder()
                .apply {
                    baseUrl("http://localhost:$port/")
                    client(okHttpClient(timeoutMillis))
                    addCallAdapterFactory(
                        NetworkEitherCallAdapterFactory(isNetworkAvailable = { isNetworkAvailable })
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
