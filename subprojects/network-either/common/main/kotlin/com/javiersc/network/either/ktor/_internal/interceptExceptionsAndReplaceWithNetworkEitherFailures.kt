package com.javiersc.network.either.ktor._internal

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpSendPipeline

internal fun interceptExceptionsAndReplaceWithNetworkEitherFailures(
    client: HttpClient,
    isNetAvailable: suspend () -> Boolean,
) {
    client.sendPipeline.intercept(HttpSendPipeline.State) {
        if (isNetAvailable()) {
            try {
                proceed()
            } catch (throwable: Throwable) {
                proceedWith(fakeHttpFailureClientCall(client))
            }
        } else {
            try {
                proceedWith(fakeLocalFailureClientCall(client))
            } catch (throwable: Throwable) {
                proceedWith(fakeLocalFailureClientCall(client))
            }
        }
    }
}
