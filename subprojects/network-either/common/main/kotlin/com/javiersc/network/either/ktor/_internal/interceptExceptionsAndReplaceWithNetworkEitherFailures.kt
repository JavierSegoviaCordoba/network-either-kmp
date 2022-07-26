package com.javiersc.network.either.ktor._internal

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpSendPipeline

internal fun interceptExceptionsAndReplaceWithNetworkEitherFailures(
    scope: HttpClient,
    isNetAvailable: () -> Boolean,
) {
    scope.sendPipeline.intercept(HttpSendPipeline.State) {
        if (!isNetAvailable()) {
            val outgoingContent = LocalErrorOutgoing
            proceedWith(outgoingContent)
        } else {
            try {
                proceed()
            } catch (throwable: Throwable) {
                val outgoingContent = RemoteErrorOutgoing
                proceedWith(fakeFailureHttpClientCall(scope, outgoingContent))
            }
        }
    }
}
