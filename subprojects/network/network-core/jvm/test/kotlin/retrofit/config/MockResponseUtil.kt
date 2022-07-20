package com.javiersc.either.network.retrofit.config

import com.javiersc.either.network.Headers
import com.javiersc.either.network.readResource
import mockwebserver3.MockResponse

internal fun mockResponse(codeToFile: Pair<Int, String?>, headers: Headers): MockResponse {
    val (code: Int, file: String?) = codeToFile
    return MockResponse().apply {
        setResponseCode(code)
        file?.let { setBody(readResource(it)) }
        headers.forEach { setHeader(it.key, it.value.first()) }
    }
}
