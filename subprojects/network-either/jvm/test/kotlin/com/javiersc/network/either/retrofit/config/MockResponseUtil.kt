package com.javiersc.network.either.retrofit.config

import com.javiersc.network.either.Headers
import com.javiersc.network.either.readResource
import mockwebserver3.MockResponse

internal fun mockResponse(codeToFile: Pair<Int, String?>, headers: Headers): MockResponse {
    val (code: Int, file: String?) = codeToFile
    return MockResponse().apply {
        setResponseCode(code)
        file?.let { setBody(readResource(it)) }
        headers.forEach { setHeader(it.key, it.value.first()) }
    }
}
