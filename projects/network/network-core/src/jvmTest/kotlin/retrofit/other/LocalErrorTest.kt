package com.javiersc.either.network.retrofit.other

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.either.network.retrofit.config.DogService
import com.javiersc.either.network.runTestBlocking
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import okhttp3.mockwebserver.MockWebServer

internal class LocalErrorTest : BaseTest<ErrorDTO, DogDTO>() {

    override val mockWebServer: MockWebServer
        get() = MockWebServer()
    override val service: DogService
        get() = DogService.getServicefailureLocal(mockWebServer.url("/"))
    override val codeToFile: Pair<Int, String?>
        get() = 200 to null
    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureLocal()

    @Test fun `suspend call`() = runTestBlocking { service.getDog() shouldBe expected }

    @Test fun `async call`() = runTestBlocking { service.getDogAsync().await() shouldBe expected }
}
