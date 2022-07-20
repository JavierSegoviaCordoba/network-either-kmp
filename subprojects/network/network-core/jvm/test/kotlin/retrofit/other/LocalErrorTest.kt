package com.javiersc.either.network.retrofit.other

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.either.network.retrofit.config.DogService
import io.kotest.matchers.shouldBe
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class LocalErrorTest : BaseTest<ErrorDTO, DogDTO>() {

    override val service: DogService
        get() = DogService.getServicefailureLocal(serverRule.server.url("/"))

    override val codeToFile: Pair<Int, String?>
        get() = 200 to null

    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureLocal()

    @Test fun `suspend call`() = runTest { service.getDog() shouldBe expected }

    @Ignore @Test fun `async call`() = runTest { service.getDogAsync().await() shouldBe expected }
}
