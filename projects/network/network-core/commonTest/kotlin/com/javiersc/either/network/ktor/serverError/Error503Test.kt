package com.javiersc.either.network.ktor.serverError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Error503Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 503 to "5xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureHttp(error, code, headers)

    @Test
    fun `Request 503`() = runTest {
        networkEitherKtor<ErrorDTO, DogDTO> { get("path") } shouldBe expected
    }
}