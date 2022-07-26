package com.javiersc.network.either._config.fakes

import kotlinx.serialization.json.Json

internal val defaultJson = Json {
    prettyPrint = true
    isLenient = false
    encodeDefaults = true
    ignoreUnknownKeys = true
}
