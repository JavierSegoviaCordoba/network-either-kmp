package com.javiersc.either.network.ktor.success

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.ktor.BaseNullTest
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import kotlin.test.Test

internal class Success204Test : BaseNullTest<ErrorDTO>() {

    override val codeToFile = 204 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, Unit> = buildNetworkSuccess(dog, code, headers)

    @Test
    fun `Request 204`() = suspendTest {
        networkEitherKtor<ErrorDTO, Unit> { get("path") } shouldBe expected
    }
}
