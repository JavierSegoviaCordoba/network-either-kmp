package com.javiersc.either.network.ktor.success

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.ktor.BaseTest
import com.javiersc.either.network.ktor.NetworkEither
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.runTestBlocking
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import kotlin.test.Test

internal class Success201Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile = 201 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkSuccess(dog, code, headers)

    @Test
    fun `Request 201`() = runTestBlocking {
        NetworkEither<ErrorDTO, DogDTO> { client.get("path") } shouldBe expected
    }
}
