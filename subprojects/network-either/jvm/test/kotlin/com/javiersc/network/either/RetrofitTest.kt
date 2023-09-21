package com.javiersc.network.either

import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.NetworkEither.Companion.localFailure
import com.javiersc.network.either.NetworkEither.Companion.remoteFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import com.javiersc.network.either._config.DogService
import com.javiersc.network.either._config.fakes.clientErrorCodes
import com.javiersc.network.either._config.fakes.headers
import com.javiersc.network.either._config.fakes.serverErrorCodes
import com.javiersc.network.either._config.fakes.successCodes
import com.javiersc.network.either._config.fakes.toResourceJsonFile
import com.javiersc.network.either._config.mockResponse
import com.javiersc.network.either._config.models.DogDTO
import com.javiersc.network.either._config.models.ErrorDTO
import com.javiersc.network.either._config.models.dogDTO
import com.javiersc.network.either._config.models.errorDTO
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.property.checkAll
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest

internal class RetrofitTest {
    @Test
    fun `call 2xx`() = runTest {
        val port = 58200
        val server: ClientAndServer = ClientAndServer.startClientAndServer(port)
        server.apply {
            for (code in 200..299) {
                `when`(HttpRequest.request().withPath("/dog/$code"))
                    .respond(mockResponse(code, code.toResourceJsonFile(), headers(code)))
            }
        }

        val service = DogService.getService(port)
        checkAll(iterations = successCodes.minIterations(), successCodes) { code ->
            when (code) {
                in 204..205 -> {
                    val expected: NetworkEither<ErrorDTO, Unit> = success(Unit, code, headers(code))
                    service.getDog(code) shouldBe expected
                    service.getDogAsync(code).await() shouldBe expected
                    service.getDogWithoutBody(code) shouldBe expected
                    service.getDogWithoutBodyAsync(code).await() shouldBe expected
                }
                in 200..299 -> {
                    val expected: NetworkEither<ErrorDTO, DogDTO> =
                        success(dogDTO, code, headers(code))
                    service.getDog(code) shouldBe expected
                    service.getDogAsync(code).await() shouldBe expected
                }
            }
        }
    }

    @Test
    fun `call 4xx`() = runTest {
        val port = 58400
        val server: ClientAndServer = ClientAndServer.startClientAndServer(port)
        server.apply {
            for (code in 400..499) {
                `when`(HttpRequest.request().withPath("/dog/$code"))
                    .respond(mockResponse(code, code.toResourceJsonFile(), headers(code)))
            }
        }

        val service = DogService.getService(port)
        checkAll(iterations = clientErrorCodes.minIterations(), clientErrorCodes) { code ->
            val expected: NetworkEither<ErrorDTO, DogDTO> =
                httpFailure(errorDTO, code, headers(code))
            service.getDog(code) shouldBe expected
            service.getDogAsync(code).await() shouldBe expected
        }
    }

    @Test
    fun `call 5xx`() = runTest {
        val port = 58500
        val server: ClientAndServer = ClientAndServer.startClientAndServer(port)
        server.apply {
            for (code in 500..599) {
                `when`(HttpRequest.request().withPath("/dog/$code"))
                    .respond(mockResponse(code, code.toResourceJsonFile(), headers(code)))
            }
        }

        val service = DogService.getService(port)
        checkAll(iterations = serverErrorCodes.minIterations(), serverErrorCodes) { code ->
            val expected: NetworkEither<ErrorDTO, DogDTO> =
                httpFailure(errorDTO, code, headers(code))
            service.getDog(code) shouldBe expected
            service.getDogAsync(code).await() shouldBe expected
        }
    }

    @Test
    fun `call local failure`() = runTest {
        val service = DogService.getService(58999, isNetworkAvailable = false)
        val expected: NetworkEither<ErrorDTO, DogDTO> = localFailure()
        service.getDog(200) shouldBe expected
        service.getDogAsync(200).await() shouldBe expected
    }

    @Test
    fun `call remote failure`() = runTest {
        val service = DogService.getService(58999)
        val expected: NetworkEither<ErrorDTO, DogDTO> = remoteFailure()
        service.getDog(200) shouldBe expected
        service.getDogAsync(200).await() shouldBe expected
    }

    @Test
    fun `call malformed json`() = runTest {
        val port = 58201
        val server: ClientAndServer = ClientAndServer.startClientAndServer(port)
        val code = 200

        server
            .`when`(HttpRequest.request().withPath("/dog/$code"))
            .respond(mockResponse(code, "malformed.json", headers(code, "malformed.json")))

        val service = DogService.getService(port)
        val expected: NetworkEither<ErrorDTO, DogDTO> =
            unknownFailure(Exception("Expected end of the object"))
        service
            .getDog(code)
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .throwable
            .message
            .shouldContain(expected.shouldBeTypeOf<NetworkFailureUnknown>().throwable.message!!)

        service
            .getDogAsync(code)
            .await()
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .throwable
            .message
            .shouldContain(expected.shouldBeTypeOf<NetworkFailureUnknown>().throwable.message!!)
    }
}
