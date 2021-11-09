package com.javiersc.either.network.retrofit.success

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseNullTest
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.shouldBe
import kotlin.test.Ignore
import kotlin.test.Test

internal class Success205Test : BaseNullTest<ErrorDTO>() {

    override val codeToFile: Pair<Int, String?> = 205 to null
    override val expected: NetworkEither<ErrorDTO, Unit> = buildNetworkSuccess(dog, code, headers)

    @Test fun `suspend call 205`() = suspendTest { service.getDog() shouldBe expected }

    @Test
    @Ignore("Parallel execution breaks, refactor to use RetrofitMock or another framework")
    fun `async call 205`() = suspendTest { service.getDogAsync().await() shouldBe expected }
}
