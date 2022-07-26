package com.javiersc.network.either._config

import com.javiersc.network.either.Headers
import org.mockserver.model.HttpResponse

internal fun mockResponse(code: Int, file: String?, headers: Headers): HttpResponse =
    HttpResponse.response().withStatusCode(code).apply {
        file?.let { withBody(readResource(it)) }
        headers.forEach { withHeader(it.key, it.value.first()) }
    }
