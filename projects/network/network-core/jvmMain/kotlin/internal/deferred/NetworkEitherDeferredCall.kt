package com.javiersc.either.network.internal.deferred

import com.javiersc.either.network.HttpStatusCode as NetworkHttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.buildNetworkFailureRemote
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.internal.hasBody
import com.javiersc.either.network.internal.utils.emptyHeader
import com.javiersc.either.network.internal.utils.printlnError
import com.javiersc.either.network.internal.utils.printlnWarning
import io.ktor.http.HttpStatusCode
import io.ktor.util.toMap
import java.io.EOFException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response

internal fun <F : Any, S : Any> deferredAdapt(
    call: Call<S>,
    errorConverter: Converter<ResponseBody, F>,
    isNetworkAvailable: () -> Boolean,
): CompletableDeferred<NetworkEither<F, S>> {
    val deferred = CompletableDeferred<NetworkEither<F, S>>()

    deferred.invokeOnCompletion { if (deferred.isCancelled) call.cancel() }

    call.enqueue(
        object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                response.responseDeferredHandler(errorConverter, deferred)
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                when (throwable) {
                    is UnknownHostException, is ConnectException, is InterruptedIOException ->
                        onCommonConnectionException(deferred, isNetworkAvailable())
                    is EOFException -> onEOFException(deferred)
                    is IllegalStateException -> onIllegalStateException(deferred, throwable)
                    is HttpException -> onHttpException(deferred, errorConverter, throwable)
                    is SerializationException ->
                        if (throwable.hasBody) onIllegalStateException(deferred, throwable)
                        else onEOFException(deferred)
                    else -> deferred.complete(buildNetworkFailureUnknown(throwable))
                }
            }
        }
    )
    return deferred
}

private fun <F, S> onEOFException(deferred: CompletableDeferred<NetworkEither<F, S>>) {
    printlnWarning(
        """
           | # # # # # # # # # # # # # # WARNING # # # # # # # # # # # # # # # # # # #
           | # Every 2XX response should have a body except 204/205, as the response #
           | # was empty, the response is transformed to Success with code 204 and   #
           | # the headers are lost. The type should be Unit.                        #
           | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        """.trimMargin()
    )

    @Suppress("UNCHECKED_CAST")
    try {
        deferred.complete(
            buildNetworkSuccess(
                Unit as S,
                NetworkHttpStatusCode(HttpStatusCode.NoContent.value),
                emptyHeader.toMap()
            )
        )
    } catch (e: ClassCastException) {
        printlnError(
            """
               | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # # # # #
               | # NetworkResponse should use Unit as Success type when body is null #
               | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
            """.trimMargin()
        )
        deferred.complete(buildNetworkFailureUnknown(e))
    }
}

private fun <F, S> onIllegalStateException(
    deferred: CompletableDeferred<NetworkEither<F, S>>,
    throwable: Throwable
) {
    printlnError(
        """
           | # # # # # # # # # # # # # # ERROR # # # # # # # # # # # # # # #
           | # Response body can't be serialized with the object provided  #
           | # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
        """.trimMargin()
    )
    deferred.complete(buildNetworkFailureUnknown(throwable))
}

private fun <F, S> onCommonConnectionException(
    deferred: CompletableDeferred<NetworkEither<F, S>>,
    isNetworkAvailable: Boolean
) {
    deferred.complete(
        if (isNetworkAvailable) buildNetworkFailureRemote() else buildNetworkFailureLocal()
    )
}

private fun <F : Any, S : Any> onHttpException(
    deferred: CompletableDeferred<NetworkEither<F, S>>,
    errorConverter: Converter<ResponseBody, F>,
    exception: HttpException,
) = exception.httpExceptionDeferredHandler(errorConverter, deferred)
