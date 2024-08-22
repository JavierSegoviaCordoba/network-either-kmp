package com.javiersc.network.either.internal.suspend

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.localFailure
import com.javiersc.network.either.NetworkEither.Companion.remoteFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import com.javiersc.network.either.internal.hasBody
import com.javiersc.network.either.internal.utils.emptyHeader
import com.javiersc.network.either.internal.utils.printlnError
import com.javiersc.network.either.internal.utils.printlnWarning
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.util.toMap
import java.io.EOFException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response

internal class NetworkEitherSuspendCall<F : Any, S : Any>(
    private val backingCall: Call<S>,
    private val errorConverter: Converter<ResponseBody, F>,
    private val isNetworkAvailable: () -> Boolean,
) : Call<NetworkEither<F, S>> {

    override fun enqueue(callback: Callback<NetworkEither<F, S>>) =
        synchronized(this) {
            backingCall.enqueue(
                object : Callback<S> {
                    override fun onResponse(call: Call<S>, response: Response<S>) {
                        response.responseSuspendHandler(
                            errorConverter,
                            this@NetworkEitherSuspendCall,
                            callback,
                        )
                    }

                    override fun onFailure(call: Call<S>, throwable: Throwable) {
                        if (!isNetworkAvailable()) {
                            onCommonConnectionExceptions(callback, isNetworkAvailable())
                        } else {
                            when (throwable) {
                                is UnknownHostException,
                                is ConnectException,
                                is InterruptedIOException -> {
                                    onCommonConnectionExceptions(callback, isNetworkAvailable())
                                }
                                is EOFException -> onEOFException(callback)
                                is IllegalStateException -> {
                                    onIllegalStateException(callback, throwable)
                                }
                                is HttpException -> {
                                    onHttpException(callback, errorConverter, throwable)
                                }
                                is SerializationException -> {
                                    if (throwable.hasBody) {
                                        onIllegalStateException(callback, throwable)
                                    } else {
                                        onEOFException(callback)
                                    }
                                }
                                else -> {
                                    Response.success(unknownFailure(throwable))
                                }
                            }
                        }
                    }
                }
            )
        }

    override fun isExecuted(): Boolean = synchronized(this) { backingCall.isExecuted }

    override fun clone(): Call<NetworkEither<F, S>> =
        NetworkEitherSuspendCall(backingCall.clone(), errorConverter, isNetworkAvailable)

    override fun isCanceled(): Boolean = synchronized(this) { backingCall.isCanceled }

    override fun cancel() = synchronized(this) { backingCall.cancel() }

    override fun execute(): Response<NetworkEither<F, S>> =
        throw UnsupportedOperationException("Suspend call does not support synchronous execution")

    override fun request(): Request = backingCall.request()

    override fun timeout(): Timeout = backingCall.timeout()
}

private fun <F, S> Call<NetworkEither<F, S>>.onEOFException(
    callback: Callback<NetworkEither<F, S>>
) {
    printlnWarning(
        """
           | # # # # # # # # # # # # # # WARNING # # # # # # # # # # # # # # # # # # #
           | # Every 2XX response should have a body except 204/205, as the response #
           | # was empty, the response is transformed to Success with code 204 and   #
           | # the headers are lost. The type should be Unit.                        #
           | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        """
            .trimMargin()
    )

    @Suppress("UNCHECKED_CAST")
    try {
        val success = success(Unit as S, NoContent.value, emptyHeader.toMap())
        callback.onResponse(this, Response.success(success))
    } catch (e: ClassCastException) {
        printlnError(
            """
               | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # # #
               | # NetworkResponse should use Unit as Success type when body is null #
               | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
            """
                .trimMargin()
        )
        callback.onResponse(this, Response.success(unknownFailure(e)))
    }
}

private fun <F, S> Call<NetworkEither<F, S>>.onIllegalStateException(
    callback: Callback<NetworkEither<F, S>>,
    throwable: Throwable,
) {
    printlnError(
        """
           | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # #
           | # Response body can't be serialized with the object provided  #
           | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        """
            .trimMargin()
    )
    callback.onResponse(this, Response.success(unknownFailure(throwable)))
}

private fun <F : Any, S : Any> NetworkEitherSuspendCall<F, S>.onCommonConnectionExceptions(
    callback: Callback<NetworkEither<F, S>>,
    isNetworkAvailable: Boolean,
) {
    callback.onResponse(
        this,
        if (isNetworkAvailable) Response.success(remoteFailure())
        else Response.success(localFailure()),
    )
}

private fun <F : Any, S : Any> NetworkEitherSuspendCall<F, S>.onHttpException(
    callback: Callback<NetworkEither<F, S>>,
    errorConverter: Converter<ResponseBody, F>,
    exception: HttpException,
) = exception.httpExceptionSuspendHandler(errorConverter, this, callback)
