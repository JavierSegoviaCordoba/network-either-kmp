package com.javiersc.either.network.ktor.clientError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Error407Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 407 to "4xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureHttp(error, code, headers)

    @Test
    fun `Request 407`() = runTest {
        networkEitherKtor<ErrorDTO, DogDTO> { get("path") } shouldBe expected
    }
}
