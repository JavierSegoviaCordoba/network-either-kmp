package com.javiersc.either.network.retrofit.other

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailureUnknown
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import kotlin.test.Test
import kotlinx.serialization.SerializationException
import org.junit.Ignore

internal class MalformedJsonTest : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 200 to "malformed.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureUnknown(SerializationException(partialMessage))

    @Test
    fun `suspend call 200 with a malformed json`() = suspendTest {
        service
            .getDog()
            .shouldBeTypeOf<NetworkFailureUnknown>()
            .left
            .throwable
            .message
            .shouldContain(
                expected.shouldBeTypeOf<NetworkFailureUnknown>().left.throwable.message!!,
            )
    }

    @Test
    @Ignore("Parallel execution breaks, refactor to use RetrofitMock or another framework")
    fun `async call  200 with a malformed json`() = suspendTest {
        service
            .getDogAsync()
            .await()
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
