package com.javiersc.either.network.ktor.serverError

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Error507Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 507 to "5xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> =
        buildNetworkFailureHttp(error, code, headers)

    @Test
    fun `Request 507`() = runTest {
        val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("path").body()
        actual shouldBe expected
    }
}
