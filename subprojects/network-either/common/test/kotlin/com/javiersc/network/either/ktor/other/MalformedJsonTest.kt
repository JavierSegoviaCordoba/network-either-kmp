package com.javiersc.network.either.ktor.other

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import com.javiersc.network.either.NetworkFailureUnknown
import com.javiersc.network.either.ktor.BaseTest
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException

internal class MalformedJsonTest : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 200 to "malformed.json"
    override val expected = unknownFailure(SerializationException(PARTIAL_MESSAGE))

    @Test
    fun `Request 200 with a malformed json`() = runTest {
        val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("path").body()
        actual
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .throwable
            .message
            .shouldContain(expected.shouldBeTypeOf<NetworkFailureUnknown>().throwable.message!!)
    }
}

private const val PARTIAL_MESSAGE = "Unexpected JSON token at offset"
