package com.javiersc.network.either.retrofit.other

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.models.DogDTO
import com.javiersc.network.either.models.ErrorDTO
import com.javiersc.network.either.retrofit.BaseTest
import com.javiersc.network.either.retrofit.config.DogService
import io.kotest.matchers.shouldBe
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class RemoteErrorTest : BaseTest<ErrorDTO, DogDTO>() {

    override val service: DogService
        get() = DogService.getService(serverRule.server.url("/"), 1L)

    override val codeToFile: Pair<Int, String?>
        get() = 200 to null

    override val expected: NetworkEither<ErrorDTO, DogDTO> = NetworkEither.remoteFailure()

    @Ignore @Test fun `suspend call`() = runTest { service.getDog() shouldBe expected }

    @Ignore @Test fun `async call`() = runTest { service.getDogAsync().await() shouldBe expected }
}
