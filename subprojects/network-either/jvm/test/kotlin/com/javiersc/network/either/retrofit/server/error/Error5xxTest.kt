package com.javiersc.network.either.retrofit.server.error

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import com.javiersc.network.either.retrofit.BaseTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Error5xxTest : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 574 to "5xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = httpFailure(error, code, headers)

    @Test fun `suspend call 5xx`() = runTest { service.getDog() shouldBe expected }

    @Test fun `async call 5xx`() = runTest { service.getDogAsync().await() shouldBe expected }
}
