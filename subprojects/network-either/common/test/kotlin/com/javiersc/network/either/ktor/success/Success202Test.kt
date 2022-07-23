package com.javiersc.network.either.ktor.success

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.ktor.BaseTest
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Success202Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 202 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = success(dog, code, headers)

    @Test
    fun `Request 202`() = runTest {
        val actual: NetworkEither<ErrorDTO, DogDTO> = client.get("path").body()
        actual shouldBe expected
    }
}
