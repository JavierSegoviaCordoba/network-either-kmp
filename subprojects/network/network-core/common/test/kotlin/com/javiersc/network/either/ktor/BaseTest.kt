package com.javiersc.network.either.ktor

import com.javiersc.network.either.Headers
import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.ktor._internal.toHttpStatusCode
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import com.javiersc.network.either.readResource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.http.hostWithPort
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.toMap
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json

internal abstract class BaseTest<F, S> {

    abstract val codeToFile: Pair<Int, String>
    abstract val expected: NetworkEither<F, S>

    val dog: DogDTO
        get() = DogDTO(1, "Auri", 7)

    val error: ErrorDTO
        get() = ErrorDTO("Dog has some error")

    val code: Int
        get() = codeToFile.first

    val headers: Headers
        get() = headersOf("Content-Type" to listOf("application/json")).toMap()

    val client: HttpClient =
        HttpClient(MockEngine) {
            install(NetworkEitherPlugin)
            install(ContentNegotiation) { json(defaultJson) }
            engine {
                addHandler { request ->
                    when (request.url.fullUrl) {
                        "http://localhost/path" -> {
                            respond(
                                content = readResource(codeToFile.second),
                                status = codeToFile.first.toHttpStatusCode("Custom HttpStatusCode"),
                                headers = headersOf(ContentType, "${Application.Json}")
                            )
                        }
                        "http://localhost/remote-unavailable" -> throw IOException("")
                        else -> error("Unhandled ${request.url.fullUrl}")
                    }
                }
            }
        }
}

internal abstract class BaseNullTest<F> {

    abstract val codeToFile: Pair<Int, String>
    abstract val expected: NetworkEither<F, Unit>

    val dog: Unit
        get() = Unit

    val code: Int
        get() = codeToFile.first

    val headers: Headers
        get() = headersOf("Content-Type" to listOf("application/json")).toMap()

    val client: HttpClient =
        HttpClient(MockEngine) {
            install(NetworkEitherPlugin)
            install(ContentNegotiation) { json(defaultJson) }
            engine {
                addHandler { request ->
                    when (request.url.fullUrl) {
                        "http://localhost/path" -> {
                            respond(
                                content = "",
                                status = codeToFile.first.toHttpStatusCode("Custom HttpStatusCode"),
                                headers = headersOf(ContentType, "${Application.Json}")
                            )
                        }
                        "http://localhost/remote-unavailable" -> throw IOException("")
                        else -> error("Unhandled ${request.url.fullUrl}")
                    }
                }
            }
        }
}

private val defaultJson = Json {
    prettyPrint = true
    isLenient = false
    encodeDefaults = true
    ignoreUnknownKeys = true
}

private val Url.hostWithPortIfRequired: String
    get() = if (port == protocol.defaultPort) host else hostWithPort

private val Url.fullUrl: String
    get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"
