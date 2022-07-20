package com.javiersc.either.network.retrofit

import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.readResource
import com.javiersc.either.network.retrofit.config.DogService
import com.javiersc.either.network.retrofit.config.mockResponse
import io.ktor.http.headersOf
import io.ktor.util.toMap
import kotlin.test.BeforeTest
import mockwebserver3.junit4.MockWebServerRule
import org.junit.Rule

internal abstract class BaseTest<F, S> {

    @JvmField @Rule val serverRule = MockWebServerRule()

    internal open val service: DogService
        get() = DogService.getService(serverRule.server.url("/"))

    abstract val codeToFile: Pair<Int, String?>

    abstract val expected: NetworkEither<F, S>

    @BeforeTest
    fun `start mockWebServer and enqueue response`() {
        serverRule.server.enqueue(mockResponse(codeToFile, headers))
    }

    val dog: DogDTO
        get() = DogDTO(1, "Auri", 7)

    val error: ErrorDTO
        get() = ErrorDTO("Dog has some error")

    val code: HttpStatusCode
        get() = HttpStatusCode(codeToFile.first)

    val headers: Headers
        get() = headersOf(contentLength, contentType).toMap()

    private val contentType: Pair<String, List<String>>
        get() = "content-type" to listOf("application/json")

    private val contentLength: Pair<String, List<String>>
        get() =
            "content-length" to
                listOf(codeToFile.second?.let { readResource(it).length.toString() } ?: "0")
}

internal abstract class BaseNullTest<F> {

    @JvmField @Rule val serverRule = MockWebServerRule()

    internal val service: DogService
        get() = DogService.getService(serverRule.server.url("/"))

    abstract val codeToFile: Pair<Int, String?>
    abstract val expected: NetworkEither<F, Unit>

    val dog
        get() = Unit
    val error
        get() = ErrorDTO("Dog has some error")
    val code: HttpStatusCode
        get() = HttpStatusCode(codeToFile.first)
    val headers: Headers
        get() = headersOf(contentLength, contentType).toMap()

    private val contentType: Pair<String, List<String>>
        get() = "content-type" to listOf("application/json")
    private val contentLength: Pair<String, List<String>>
        get() =
            "content-length" to
                listOf(codeToFile.second?.let { readResource(it).length.toString() } ?: "0")

    @BeforeTest
    fun `start mockWebServer and enqueue response`() {
        serverRule.server.enqueue(mockResponse(codeToFile, headers))
    }
}
