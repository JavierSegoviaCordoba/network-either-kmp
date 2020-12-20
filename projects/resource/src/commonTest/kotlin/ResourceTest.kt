package com.javiersc.either.resource

import com.javiersc.either.Either
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlin.test.Test

internal class ResourceTest {
    private val failure = "Error"
    private val success = "Success"

    @Test
    fun `Create Resource Failure`() {
        buildResourceFailure(failure, success, true)
            .shouldBeTypeOf<Either.Left<String>>().left
            .shouldBe(Failure(failure, success, true))
    }

    @Test
    fun `Create Resource SuccessLoading`() {
        buildResourceSuccessLoading<String, String>(success)
            .shouldBeTypeOf<Either.Right<String>>().right
            .shouldBe(Success.Loading(success))
    }

    @Test
    fun `Create Resource SuccessData`() {
        buildResourceSuccessData<String, String>(success, true)
            .shouldBeTypeOf<Either.Right<String>>().right
            .shouldBe(Success.Data(success, true))
    }
}
