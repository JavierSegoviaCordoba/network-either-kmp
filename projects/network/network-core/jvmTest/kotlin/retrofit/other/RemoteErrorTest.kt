package com.javiersc.either.network.retrofit.other

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureRemote
import com.javiersc.either.network.models.DogDTO
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseTest
import com.javiersc.either.network.retrofit.config.DogService
import io.kotest.matchers.shouldBe
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer

internal class RemoteErrorTest : BaseTest<ErrorDTO, DogDTO>() {

    override val mockWebServer: MockWebServer
        get() = MockWebServer()
    override val service: DogService
        get() = DogService.getService(mockWebServer.url("/"), 1L)
    override val codeToFile: Pair<Int, String?>
        get() = 200 to null
    override val expected: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureRemote()

    @Test fun `suspend call`() = runTest { service.getDog() shouldBe expected }

    @Test
    @Ignore("Parallel execution breaks, refactor to use RetrofitMock or another framework")
    fun `async call`() = runTest { service.getDogAsync().await() shouldBe expected }
}
