package com.javiersc.network.either.retrofit.success

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.models.ErrorDTO
import com.javiersc.network.either.retrofit.BaseNullTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class Success205Test : BaseNullTest<ErrorDTO>() {

    override val codeToFile: Pair<Int, String?> = 205 to null
    override val expected: NetworkEither<ErrorDTO, Unit> = success(dog, code, headers)

    @Test fun `suspend call 205`() = runTest { service.getDog() shouldBe expected }

    @Test fun `async call 205`() = runTest { service.getDogAsync().await() shouldBe expected }
}
