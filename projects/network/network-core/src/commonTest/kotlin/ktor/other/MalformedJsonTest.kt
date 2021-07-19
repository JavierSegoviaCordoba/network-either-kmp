package com.javiersc.either.network.ktor.other

import com.javiersc.either.network.NetworkFailureUnknown
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.runTestBlocking
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.serialization.SerializationException

internal class MalformedJsonTest : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 200 to "malformed.json"
    override val expected =
        buildNetworkFailureUnknown<ErrorDTO, DogDTO>(SerializationException(partialMessage))

    @Test
    fun `Request 200 with a malformed json`() = runTestBlocking {
        networkEitherKtor<ErrorDTO, DogDTO> { get("path") }
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .left
            .throwable
            .message
            .shouldContain(
                expected.shouldBeTypeOf<NetworkFailureUnknown>().left.throwable.message!!,
            )
    }
}

private const val partialMessage = "Unexpected JSON token at offset"
