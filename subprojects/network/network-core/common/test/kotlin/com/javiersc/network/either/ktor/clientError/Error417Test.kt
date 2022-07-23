package com.javiersc.network.either.ktor.clientError

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.ktor.BaseTest
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Error417Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 417 to "4xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = httpFailure(error, code, headers)

    @Test
    fun `Request 417`() = runTest {
        val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("path").body()
        actual shouldBe expected
    }
}
