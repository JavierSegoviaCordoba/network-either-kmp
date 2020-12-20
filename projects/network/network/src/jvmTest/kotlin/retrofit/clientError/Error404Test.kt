package com.javiersc.either.network.retrofit.clientError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.either.network.runTestBlocking
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class Error404Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 404 to "4xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureHttp(error, code, headers)

    @Test
    fun `suspend call 404`() = runTestBlocking {
        service.getDog() shouldBe expected
    }

    @Test
    fun `async call 404`() = runTestBlocking {
        service.getDogAsync().await() shouldBe expected
    }
}
