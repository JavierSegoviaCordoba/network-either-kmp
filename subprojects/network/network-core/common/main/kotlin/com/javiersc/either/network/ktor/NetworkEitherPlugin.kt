package com.javiersc.either.network.ktor

import com.javiersc.either.network.ktor._internal.interceptBeforeTransformAndReplaceContainerWithNetworkEitherType
import com.javiersc.either.network.ktor._internal.interceptExceptionsAndReplaceWithNetworkEitherFailures
import com.javiersc.either.network.ktor._internal.interceptSuccessesAndReplaceWithNetworkEitherSuccess
import com.javiersc.either.network.utils.isNetworkAvailable
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.util.AttributeKey

public class NetworkEitherPlugin(
    private val mockIsNetworkAvailable: () -> Boolean = { isNetworkAvailable }
) {

    public class Config {
        public var mockIsNetworkAvailable: () -> Boolean = { isNetworkAvailable }
    }

    public companion object : HttpClientPlugin<Config, NetworkEitherPlugin> {

        override val key: AttributeKey<NetworkEitherPlugin> = AttributeKey("NetworkEither")

        override fun install(plugin: NetworkEitherPlugin, scope: HttpClient) {
            interceptExceptionsAndReplaceWithNetworkEitherFailures(scope)
            interceptBeforeTransformAndReplaceContainerWithNetworkEitherType(scope)
            interceptSuccessesAndReplaceWithNetworkEitherSuccess(scope)
        }

        override fun prepare(block: Config.() -> Unit): NetworkEitherPlugin {
            val config = Config().apply { block() }
            return NetworkEitherPlugin(config.mockIsNetworkAvailable)
        }
    }
}