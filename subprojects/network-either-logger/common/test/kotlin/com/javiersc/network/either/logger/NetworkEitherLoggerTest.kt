package com.javiersc.network.either.logger

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.NetworkEither.Companion.localFailure
import com.javiersc.network.either.NetworkEither.Companion.remoteFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import io.kotest.matchers.shouldBe
import io.ktor.http.headersOf
import io.ktor.util.toMap
import kotlin.test.Test
import kotlinx.serialization.Serializable

class NetworkEitherLoggerTest {

    private val headers = headersOf("ContentType" to listOf("application/json")).toMap()

    @Test
    fun `Pretty print Success`() {
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = success(DogDTO("Auri"), 200, headers)
        val tag = "Success test"

        networkResponse.alsoLog(tag) shouldBe networkResponse
    }

    @Test
    fun `Pretty print Http failure`() {
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> =
            httpFailure(ErrorDTO(":("), 404, headers)
        val tag = "Error test"

        networkResponse.alsoLog(tag) shouldBe networkResponse
    }

    @Test
    fun `Pretty print Local failure`() {
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = localFailure()
        val tag = "Internet not available test"

        networkResponse.alsoLog(tag) shouldBe networkResponse
    }

    @Test
    fun `Pretty print Remote failure`() {
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = remoteFailure()
        val tag = "Remote not available test"

        networkResponse.alsoLog(tag) shouldBe networkResponse
    }

    @Test
    fun `Pretty print Unknown failure`() {
        val exception = IllegalStateException("Unknown error")
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = unknownFailure(exception)
        val tag = "Unknown error test"

        networkResponse.alsoLog(tag) shouldBe networkResponse
    }
}

@Serializable private data class DogDTO(val name: String)

@Serializable private data class ErrorDTO(val error: String)
