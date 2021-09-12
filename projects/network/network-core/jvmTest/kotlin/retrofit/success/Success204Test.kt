package com.javiersc.either.network.retrofit.success

import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.models.ErrorDTO
import com.javiersc.either.network.retrofit.BaseNullTest
import com.javiersc.runBlocking.suspendTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class Success204Test : BaseNullTest<ErrorDTO>() {

    override val codeToFile: Pair<Int, String?> = 204 to null
    override val expected: NetworkEither<ErrorDTO, Unit> = buildNetworkSuccess(dog, code, headers)

    @Test fun `suspend call 204`() = suspendTest { service.getDog() shouldBe expected }

    @Test fun `async call 204`() = suspendTest { service.getDogAsync().await() shouldBe expected }
}
