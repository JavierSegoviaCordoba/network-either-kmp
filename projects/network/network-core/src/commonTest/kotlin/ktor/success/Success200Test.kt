package com.javiersc.either.network.ktor.success

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.runTestBlocking
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import kotlin.test.Test

internal class Success200Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 200 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkSuccess(dog, code, headers)

    @Test
    fun `Request 200`() = runTestBlocking {
        networkEitherKtor<ErrorDTO, DogDTO> { get("path") } shouldBe expected
    }
}
