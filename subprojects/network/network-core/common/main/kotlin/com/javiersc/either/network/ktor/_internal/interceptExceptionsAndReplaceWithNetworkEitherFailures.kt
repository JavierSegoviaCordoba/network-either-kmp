package com.javiersc.either.network.ktor._internal

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpSendPipeline

internal fun interceptExceptionsAndReplaceWithNetworkEitherFailures(scope: HttpClient) {
    scope.sendPipeline.intercept(HttpSendPipeline.Before) {
        try {
            proceed()
        } catch (throwable: Throwable) {
            val outgoingContent = RemoteErrorOutgoing
            proceedWith(scope.fakeFailureHttpClientCall(outgoingContent))
        }
    }
}
