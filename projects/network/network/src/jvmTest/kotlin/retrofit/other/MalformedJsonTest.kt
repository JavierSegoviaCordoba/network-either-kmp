package com.javiersc.either.network.retrofit.other

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailureUnknown
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.either.network.runTestBlocking
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.serialization.SerializationException
import kotlin.test.Test

internal class MalformedJsonTest : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 200 to "malformed.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureUnknown(SerializationException(message))

    @Test
    fun `suspend call 200 with a malformed json`() = runTestBlocking {
        service.getDog().shouldBeTypeOf<NetworkFailureUnknown>()
            .left.throwable.message.shouldBe(expected.shouldBeTypeOf<NetworkFailureUnknown>().left.throwable.message)
    }

    @Test
    fun `async call  200 with a malformed json`() = runTestBlocking {
        service.getDogAsync().await().shouldBeTypeOf<NetworkFailureUnknown>()
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
