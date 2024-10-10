package com.javiersc.network.either.ktor

import com.javiersc.network.either.ktor._internal.interceptBeforeTransformAndReplaceContainerWithNetworkEitherType
import com.javiersc.network.either.ktor._internal.interceptExceptionsAndReplaceWithNetworkEitherFailures
import com.javiersc.network.either.ktor._internal.interceptSuccessesAndReplaceWithNetworkEitherSuccess
import com.javiersc.network.either.utils.isNetworkAvailable as isNetAvailable
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.util.AttributeKey

public class NetworkEitherPlugin(
    private val isNetworkAvailable: suspend () -> Boolean = { isNetAvailable() }
) {

    public class Config {
        public var isNetworkAvailable: suspend () -> Boolean = { isNetAvailable() }
    }

    public companion object : HttpClientPlugin<Config, NetworkEitherPlugin> {

        override val key: AttributeKey<NetworkEitherPlugin> = AttributeKey("NetworkEither")

        override fun install(plugin: NetworkEitherPlugin, scope: HttpClient) {
            interceptExceptionsAndReplaceWithNetworkEitherFailures(scope, plugin.isNetworkAvailable)
            interceptBeforeTransformAndReplaceContainerWithNetworkEitherType(scope)
            interceptSuccessesAndReplaceWithNetworkEitherSuccess(scope)
        }

        override fun prepare(block: Config.() -> Unit): NetworkEitherPlugin {
            val config = Config().apply { block() }
            return NetworkEitherPlugin(config.isNetworkAvailable)
        }
    }
}
