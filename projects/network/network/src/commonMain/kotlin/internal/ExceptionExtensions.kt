package com.javiersc.either.network.internal

import kotlinx.serialization.SerializationException

internal val SerializationException.hasBody: Boolean
    get() = !this.message?.substringAfter("JSON input:").isNullOrBlank()
