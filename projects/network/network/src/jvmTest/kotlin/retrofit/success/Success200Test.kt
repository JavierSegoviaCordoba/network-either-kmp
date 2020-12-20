package com.javiersc.either.network.retrofit.success

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.either.network.runTestBlocking
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class Success200Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 200 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkSuccess(dog, code, headers)

    @Test
    fun `suspend call 200`() = runTestBlocking {
        service.getDog() shouldBe expected
    }

    @Test
    fun `async call 200`() = runTestBlocking {
        service.getDogAsync().await() shouldBe expected
    }
}
