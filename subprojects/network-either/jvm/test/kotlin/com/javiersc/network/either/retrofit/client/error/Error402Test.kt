package com.javiersc.network.either.retrofit.client.error

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import com.javiersc.network.either.retrofit.BaseTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Error402Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 402 to "4xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = httpFailure(error, code, headers)

    @Test fun `suspend call 402`() = runTest { service.getDog() shouldBe expected }

    @Test fun `async call 402`() = runTest { service.getDogAsync().await() shouldBe expected }
}
