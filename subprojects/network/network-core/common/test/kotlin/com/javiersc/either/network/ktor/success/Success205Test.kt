package com.javiersc.either.network.ktor.success

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.ktor.BaseNullTest
import com.javiersc.either.network.models.ErrorDTO
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Success205Test : BaseNullTest<ErrorDTO>() {

    override val codeToFile = 204 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, Unit> = buildNetworkSuccess(dog, code, headers)

    @Test
    fun `Request 205`() = runTest {
        val actual: NetworkEither<ErrorDTO, Unit> = client.get("path").body()
        actual shouldBe expected
    }
}
