package com.javiersc.either.network.retrofit.clientError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.shouldBe
import kotlin.test.Ignore
import kotlin.test.Test

internal class Error400Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 400 to "4xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureHttp(error, code, headers)

    @Test fun `suspend call 400`() = suspendTest { service.getDog() shouldBe expected }

    @Test
    @Ignore("Parallel execution breaks, refactor to use RetrofitMock or another framework")
    fun `async call 400`() = suspendTest { service.getDogAsync().await() shouldBe expected }
}
