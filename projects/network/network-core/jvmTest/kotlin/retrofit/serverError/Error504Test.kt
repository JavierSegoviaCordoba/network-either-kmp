package com.javiersc.either.network.retrofit.serverError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class Error504Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 504 to "5xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureHttp(error, code, headers)

    @Test fun `suspend call 504`() = suspendTest { service.getDog() shouldBe expected }

    @Test fun `async call 504`() = suspendTest { service.getDogAsync().await() shouldBe expected }
}