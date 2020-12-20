package com.javiersc.either.network.retrofit

import com.javiersc.either.network.Headers
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.readResource
import com.javiersc.either.network.retrofit.config.DogService
import com.javiersc.either.network.retrofit.config.mockResponse
import io.ktor.http.headersOf
import io.ktor.util.toMap
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

internal abstract class BaseTest<F, S> {

    internal open val mockWebServer = MockWebServer()
    internal open val service: DogService get() = DogService.getService(mockWebServer.url("/"))

    abstract val codeToFile: Pair<Int, String?>
    abstract val expected: NetworkEither<F, S>

    val dog get() = DogDTO(1, "Auri", 7)
    val error get() = ErrorDTO("Dog has some error")
    val code: Int get() = codeToFile.first
    val headers: Headers get() = headersOf(contentLength, contentType).toMap()

    private val contentType: Pair<String, List<String>> get() = "content-type" to listOf("application/json")
    private val contentLength: Pair<String, List<String>>
        get() = "content-length" to listOf(codeToFile.second?.let { readResource(it).length.toString() } ?: "0")

    @BeforeTest
    fun `start mockWebServer`() {
        mockWebServer.start()
    }

    @BeforeTest
    fun `enqueue response`() {
        mockWebServer.enqueue(mockResponse(codeToFile, headers))
    }

    @AfterTest
    fun `close mockWebServer`() {
        mockWebServer.shutdown()
    }
}

internal abstract class BaseNullTest<F> {

    private val mockWebServer = MockWebServer()
    internal val service: DogService get() = DogService.getService(mockWebServer.url("/"))

    abstract val codeToFile: Pair<Int, String?>
    abstract val expected: NetworkEither<F, Unit>

    val dog get() = Unit
    val error get() = ErrorDTO("Dog has some error")
    val code: Int get() = codeToFile.first
    val headers: Headers get() = headersOf(contentLength, contentType).toMap()

    private val contentType: Pair<String, List<String>> get() = "content-type" to listOf("application/json")
    private val contentLength: Pair<String, List<String>>
        get() = "content-length" to listOf(codeToFile.second?.let { readResource(it).length.toString() } ?: "0")

    @BeforeTest
    fun `start mockWebServer`() {
        mockWebServer.start()
    }

    @BeforeTest
    fun `enqueue response`() {
        mockWebServer.enqueue(mockResponse(codeToFile, headers))
    }

    @AfterTest
    fun `close mockWebServer`() {
        mockWebServer.shutdown()
    }
}
