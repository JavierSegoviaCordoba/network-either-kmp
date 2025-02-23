package com.javiersc.network.either

import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.NetworkEither.Companion.localFailure
import com.javiersc.network.either.NetworkEither.Companion.remoteFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import com.javiersc.network.either._config.DogService
import com.javiersc.network.either._config.fakes.JsonMalformed
import com.javiersc.network.either._config.fakes.clientErrorCodes
import com.javiersc.network.either._config.fakes.getJsonResponse
import com.javiersc.network.either._config.fakes.headers
import com.javiersc.network.either._config.fakes.serverErrorCodes
import com.javiersc.network.either._config.fakes.successCodes
import com.javiersc.network.either._config.mockResponse
import com.javiersc.network.either._config.models.DogDTO
import com.javiersc.network.either._config.models.ErrorDTO
import com.javiersc.network.either._config.models.dogDTO
import com.javiersc.network.either._config.models.errorDTO
import io.kotest.assertions.retry
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.property.checkAll
import kotlin.random.Random
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest

class RetrofitTest {

    @Test
    fun `call 2xx`() = runTest {
        val server: ClientAndServer = startServer()
        server.apply {
            for (code in 200..299) {
                `when`(HttpRequest.request().withPath("/dog/$code"))
                    .respond(mockResponse(code, getJsonResponse(code), headers(code)))
            }
        }

        val service: DogService = DogService.getService(server.port)
        checkAll(iterations = successCodes.minIterations(), successCodes) { code ->
            retry(maxRetry = 10, timeout = 3.seconds) {
                when (code) {
                    in 204..205 -> {
                        val expected: NetworkEither<ErrorDTO, Unit> =
                            success(Unit, code, headers(code))
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
        server.stop()
    }

    @Test
    fun `call 4xx`() = runTest {
        val server: ClientAndServer = startServer()
        server.apply {
            for (code in 400..499) {
                `when`(HttpRequest.request().withPath("/dog/$code"))
                    .respond(mockResponse(code, getJsonResponse(code), headers(code)))
            }
        }

        val service: DogService = DogService.getService(server.port)
        checkAll(iterations = clientErrorCodes.minIterations(), clientErrorCodes) { code ->
            retry(maxRetry = 10, timeout = 3.seconds) {
                val expected: NetworkEither<ErrorDTO, DogDTO> =
                    httpFailure(errorDTO, code, headers(code))
                service.getDog(code) shouldBe expected
                service.getDogAsync(code).await() shouldBe expected
            }
        }
        server.stop()
    }

    @Test
    fun `call 5xx`() = runTest {
        val server: ClientAndServer = startServer()
        server.apply {
            for (code in 500..599) {
                `when`(HttpRequest.request().withPath("/dog/$code"))
                    .respond(mockResponse(code, getJsonResponse(code), headers(code)))
            }
        }

        val service: DogService = DogService.getService(server.port)
        checkAll(iterations = serverErrorCodes.minIterations(), serverErrorCodes) { code ->
            retry(maxRetry = 10, timeout = 3.seconds) {
                val expected: NetworkEither<ErrorDTO, DogDTO> =
                    httpFailure(errorDTO, code, headers(code))
                service.getDog(code) shouldBe expected
                service.getDogAsync(code).await() shouldBe expected
            }
        }
        server.stop()
    }

    @Test
    fun `call local failure`() = runTest {
        val port: Int = Random.nextInt(49152, 65535)
        val service: DogService = DogService.getService(port, isNetworkAvailable = false)
        val expected: NetworkEither<ErrorDTO, DogDTO> = localFailure()
        service.getDog(200) shouldBe expected
        service.getDogAsync(200).await() shouldBe expected
    }

    @Test
    fun `call remote failure`() = runTest {
        val service: DogService = DogService.getService(Random.nextInt(49152, 65535))
        val expected: NetworkEither<ErrorDTO, DogDTO> = remoteFailure()
        service.getDog(200) shouldBe expected
        service.getDogAsync(200).await() shouldBe expected
    }

    @Test
    fun `call malformed json`() = runTest {
        val server: ClientAndServer = startServer()
        val code = 200

        server
            .`when`(HttpRequest.request().withPath("/dog/$code"))
            .respond(mockResponse(code, JsonMalformed, headers(code, JsonMalformed)))

        retry(maxRetry = 10, timeout = 3.seconds) {
            val service = DogService.getService(server.port)
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
}

private fun startServer(): ClientAndServer {
    val result: Result<ClientAndServer> = runCatching {
        val port: Int = Random.nextInt(49152, 65535)
        ClientAndServer.startClientAndServer(port)
    }
    return result.fold(onSuccess = { it }, onFailure = { startServer() })
}
