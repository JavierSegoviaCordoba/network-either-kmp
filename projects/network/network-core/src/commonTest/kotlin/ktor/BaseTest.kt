package com.javiersc.either.network.ktor

import com.javiersc.either.network.Headers
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.readResource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.http.hostWithPort
import io.ktor.util.toMap
import io.ktor.utils.io.errors.IOException

internal abstract class BaseTest<F, S> {

    abstract val codeToFile: Pair<Int, String>
    abstract val expected: NetworkEither<F, S>

    val dog
        get() = DogDTO(1, "Auri", 7)
    val error
        get() = ErrorDTO("Dog has some error")
    val code: HttpStatusCode
        get() = HttpStatusCode(codeToFile.first)
    val headers: Headers
        get() = headersOf("Content-Type" to listOf("application/json")).toMap()

    private val client =
        HttpClient(MockEngine) {
            install(JsonFeature) { serializer = KotlinxSerializer() }
            engine {
                addHandler { request ->
                    val json = readResource(codeToFile.second)

                    when (request.url.fullUrl) {
                        "http://localhost/path" ->
                            respond(
                                content = json,
                                status = codeToFile.first.toHttpStatusCode("Custom HttpStatusCode"),
                                headers =
                                    headersOf(
                                        "Content-Type" to
                                            listOf(ContentType.Application.Json.toString())
                                    )
                            )
                        "http://localhost/remote-unavailable" -> throw IOException("")
                        else -> error("Unhandled ${request.url.fullUrl}")
                    }
                }
            }
        }

    val networkEitherKtor: NetworkEitherKtor = NetworkEitherKtor(client)
}

internal abstract class BaseNullTest<F> {

    abstract val codeToFile: Pair<Int, String>
    abstract val expected: NetworkEither<F, Unit>

    val dog
        get() = Unit
    val code: HttpStatusCode
        get() = HttpStatusCode(codeToFile.first)
    val headers: Headers
        get() = headersOf("Content-Type" to listOf("application/json")).toMap()

    private val client =
        HttpClient(MockEngine) {
            install(JsonFeature) { serializer = KotlinxSerializer() }
            engine {
                addHandler { request ->
                    val json = ""

                    when (request.url.fullUrl) {
                        "http://localhost/path" ->
                            respond(
                                content = json,
                                status = codeToFile.first.toHttpStatusCode("Custom HttpStatusCode"),
                                headers =
                                    headersOf(
                                        "Content-Type" to
                                            listOf(ContentType.Application.Json.toString())
                                    )
                            )
                        "http://localhost/remote-unavailable" -> throw IOException("")
                        else -> error("Unhandled ${request.url.fullUrl}")
                    }
                }
            }
        }

    val networkEitherKtor: NetworkEitherKtor = NetworkEitherKtor(client)
}

private val Url.hostWithPortIfRequired: String
    get() = if (port == protocol.defaultPort) host else hostWithPort
private val Url.fullUrl: String
    get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"
