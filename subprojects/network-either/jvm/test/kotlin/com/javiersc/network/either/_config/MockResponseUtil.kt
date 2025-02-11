package com.javiersc.network.either._config

import com.javiersc.network.either.Headers
import org.mockserver.model.HttpResponse

internal fun mockResponse(code: Int, content: String?, headers: Headers): HttpResponse =
    HttpResponse.response().withStatusCode(code).apply {
        content?.let<String, HttpResponse?>(::withBody)
        headers.forEach { withHeader(it.key, it.value.first()) }
    }
