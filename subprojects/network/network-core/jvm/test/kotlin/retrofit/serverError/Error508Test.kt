package com.javiersc.either.network.retrofit.serverError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Error508Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 508 to "5xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureHttp(error, code, headers)

    @Test fun `suspend call 508`() = runTest { service.getDog() shouldBe expected }

    @Test fun `async call 508`() = runTest { service.getDogAsync().await() shouldBe expected }
}