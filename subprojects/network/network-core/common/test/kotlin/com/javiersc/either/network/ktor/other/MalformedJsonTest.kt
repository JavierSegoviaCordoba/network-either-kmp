package com.javiersc.either.network.ktor.other

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailureUnknown
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException

internal class MalformedJsonTest : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 200 to "malformed.json"
    override val expected =
        buildNetworkFailureUnknown<ErrorDTO, DogDTO>(SerializationException(partialMessage))

    @Test
    fun `Request 200 with a malformed json`() = runTest {
        val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("path").body()
        actual
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .left
            .throwable
            .message
            .shouldContain(
                expected.shouldBeTypeOf<NetworkFailureUnknown>().left.throwable.message!!
            )
    }
}

private const val partialMessage = "Unexpected JSON token at offset"
