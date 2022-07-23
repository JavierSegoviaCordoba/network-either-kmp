package com.javiersc.network.either.ktor.success

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.ktor.BaseNullTest
import com.javiersc.network.either.models.ErrorDTO
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Success204Test : BaseNullTest<ErrorDTO>() {

    override val codeToFile = 204 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, Unit> = success(dog, code, headers)

    @Test
    fun `Request 204`() = runTest {
        val actual: NetworkEither<ErrorDTO, Unit> = client.get("path").body()
        actual shouldBe expected
    }
}
