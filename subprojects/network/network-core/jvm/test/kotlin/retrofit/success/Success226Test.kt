package com.javiersc.either.network.retrofit.success

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Success226Test : BaseTest<ErrorDTO, DogDTO>() {

    override val codeToFile: Pair<Int, String?> = 226 to "2xx.json"
    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkSuccess(dog, code, headers)

    @Test fun `suspend call 226`() = runTest { service.getDog() shouldBe expected }

    @Test fun `async call 226`() = runTest { service.getDogAsync().await() shouldBe expected }
}
