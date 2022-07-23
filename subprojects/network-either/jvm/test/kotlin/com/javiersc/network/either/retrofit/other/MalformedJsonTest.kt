package com.javiersc.network.either.retrofit.other

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkFailureUnknown
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import com.javiersc.network.either.retrofit.BaseTest
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException

internal class MalformedJsonTest : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 200 to "malformed.json"

    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        NetworkEither.unknownFailure(SerializationException(PARTIAL_MESSAGE))

    @Test
    fun `suspend call 200 with a malformed json`() = runTest {
        service
            .getDog()
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .throwable
            .message
            .shouldContain(expected.shouldBeTypeOf<NetworkFailureUnknown>().throwable.message!!)
    }

    @Test
    fun `async call  200 with a malformed json`() = runTest {
        service
            .getDogAsync()
            .await()
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .throwable
            .message
            .shouldContain(expected.shouldBeTypeOf<NetworkFailureUnknown>().throwable.message!!)
    }
}

private const val PARTIAL_MESSAGE = "Unexpected JSON token at offset"
