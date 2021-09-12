package com.javiersc.either.network.retrofit.clientError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class Error422Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 422 to "4xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureHttp(error, code, headers)

    @Test fun `suspend call 422`() = suspendTest { service.getDog() shouldBe expected }

    @Test fun `async call 422`() = suspendTest { service.getDogAsync().await() shouldBe expected }
}
