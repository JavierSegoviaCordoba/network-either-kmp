package com.javiersc.network.either

import com.javiersc.network.either._config.fakes.JsonMalformed
import com.javiersc.network.either._config.fakes.clientErrorCodes
import com.javiersc.network.either._config.fakes.defaultJson
import com.javiersc.network.either._config.fakes.getJsonResponse
import com.javiersc.network.either._config.fakes.headers
import com.javiersc.network.either._config.fakes.serverErrorCodes
import com.javiersc.network.either._config.fakes.successCodes
import com.javiersc.network.either._config.models.DogDTO
import com.javiersc.network.either._config.models.ErrorDTO
import com.javiersc.network.either._config.models.dogDTO
import com.javiersc.network.either._config.models.errorDTO
import com.javiersc.network.either.ktor.NetworkEitherPlugin
import com.javiersc.network.either.ktor._internal.toHttpStatusCode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.property.checkAll
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException

class KtorTest {

    private val client: HttpClient =
        HttpClient(MockEngine) {
            install(NetworkEitherPlugin)
            install(ContentNegotiation) { json(defaultJson) }

            engine {
                addHandler { request ->
                    val path = request.url.segments
                    val code = path.last().toIntOrNull()
                    when (request.url.encodedPath) {
                        "/dog/remote-unavailable/$code" -> throw IOException("")
                        "/dog/malformed/$code" -> {
                            checkNotNull(code) { "Code must not be null at this point" }
                            val headers: Headers = headers(code, JsonMalformed)
                            respond(
                                content = JsonMalformed,
                                status = code.toHttpStatusCode("Custom HttpStatusCode"),
                                headers = headersOf(*headers.toList().toTypedArray()),
                            )
                        }

                        "/dog/$code" -> {
                            checkNotNull(code) { "Code must not be null at this point" }
                            respond(
                                content = getJsonResponse(code),
                                status = code.toHttpStatusCode("Custom HttpStatusCode"),
                                headers = headersOf(*headers(code).toList().toTypedArray()),
                            )
                        }

                        else -> error("Unhandled ${request.url}")
                    }
                }
            }
        }

    @Test
    fun call_2xx() = runTest {
        checkAll(iterations = successCodes.minIterations(), successCodes) { code ->
            when (code) {
                in 204..205 -> {
                    val expected: NetworkEither<ErrorDTO, Unit> =
                        NetworkEither.success(Unit, code, headers(code))
                    val actual: NetworkEither<ErrorDTO, Unit> = client.get("dog/$code").body()
                    actual shouldBe expected
                }

                in 200..299 -> {
                    val expected: NetworkEither<ErrorDTO, DogDTO> =
                        NetworkEither.success(dogDTO, code, headers(code))
                    val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("dog/$code").body()
                    actual shouldBe expected
                }
            }
        }
    }

    @Test
    fun call_4xx() = runTest {
        checkAll(iterations = clientErrorCodes.minIterations(), clientErrorCodes) { code ->
            val expected: NetworkEither<ErrorDTO, DogDTO> =
                NetworkEither.httpFailure(errorDTO, code, headers(code))
            val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("dog/$code").body()
            actual shouldBe expected
        }
    }

    @Test
    fun call_5xx() = runTest {
        checkAll(iterations = serverErrorCodes.minIterations(), serverErrorCodes) { code ->
            val expected: NetworkEither<ErrorDTO, DogDTO> =
                NetworkEither.httpFailure(errorDTO, code, headers(code))
            val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("dog/$code").body()
            actual shouldBe expected
        }
    }

    @Test
    fun call_local_failure() = runTest {
        val code = 200
        val localClient =
            HttpClient(MockEngine) {
                install(NetworkEitherPlugin) { isNetworkAvailable = { false } }
                install(ContentNegotiation) { json(defaultJson) }

                engine {
                    addHandler {
                        respond(
                            content = getJsonResponse(code),
                            status = code.toHttpStatusCode("Custom HttpStatusCode"),
                            headers = headersOf(*headers(code).toList().toTypedArray()),
                        )
                    }
                }
            }
        val expected: NetworkEither<ErrorDTO, DogDTO> = NetworkEither.localFailure()
        val actual: NetworkEither<ErrorDTO, DogDTO> = localClient.get("dog/$code").body()
        actual shouldBe expected
    }

    @Test
    fun call_remote_failure() = runTest {
        val code = 200
        val expected: NetworkEither<ErrorDTO, DogDTO> = NetworkEither.remoteFailure()
        val actual: NetworkEither<ErrorDTO, DogDTO> =
            client.get("dog/remote-unavailable/$code").body()
        actual shouldBe expected
    }

    @Test
    fun call_malformed_json() = runTest {
        val code = 200
        val expected: NetworkEither<ErrorDTO, DogDTO> =
            NetworkEither.unknownFailure(Exception("Expected end of the object"))
        val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("dog/malformed/$code").body()
        actual
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .throwable
            .message
            .shouldContain(expected.shouldBeTypeOf<NetworkFailureUnknown>().throwable.message!!)
    }
}
