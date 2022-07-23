package com.javiersc.network.either.retrofit.success

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import com.javiersc.network.either.retrofit.BaseTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Success201Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 201 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = success(dog, code, headers)

    @Test fun `suspend call 201`() = runTest { service.getDog() shouldBe expected }

    @Test fun `async call 201`() = runTest { service.getDogAsync().await() shouldBe expected }
}
