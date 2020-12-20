package com.javiersc.either.network.ktor.other

import com.javiersc.either.network.NetworkFailureUnknown
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.ktor.NetworkEither
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.runTestBlocking
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.client.request.get
import kotlinx.serialization.SerializationException
import kotlin.test.Test

internal class MalformedJsonTest : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 200 to "malformed.json"
    override val expected = buildNetworkFailureUnknown<ErrorDTO, DogDTO>(SerializationException(message))

    @Test
    fun `Request 200 with a malformed json`() = runTestBlocking {
        NetworkEither<ErrorDTO, DogDTO> { client.get("path") }.shouldBeTypeOf<NetworkFailureUnknown>()
            .left.throwable.message.shouldBe(expected.shouldBeTypeOf<NetworkFailureUnknown>().left.throwable.message)
    }
}

private val message =
    """
       |Unexpected JSON token at offset 41: Expected '}'
       |JSON input: {
       |  "id": 1,
       |  "name": "Auri",
       |  "age": 7
    """.trimMargin()
