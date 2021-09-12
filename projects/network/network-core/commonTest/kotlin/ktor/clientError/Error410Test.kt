package com.javiersc.either.network.ktor.clientError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import kotlin.test.Test

internal class Error410Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 410 to "4xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureHttp(error, code, headers)

    @Test
    fun `Request 410`() = suspendTest {
        networkEitherKtor<ErrorDTO, DogDTO> { get("path") } shouldBe expected
    }
}
