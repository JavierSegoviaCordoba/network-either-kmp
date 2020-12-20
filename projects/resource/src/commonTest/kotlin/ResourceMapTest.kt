package com.javiersc.either.resource

import com.javiersc.either.resource.extensions.map
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlin.test.Test

internal class ResourceMapTest {

    private val failure = 2
    private val success = 4

    @Test
    fun `Map Failure`() {
        buildResourceFailure(failure, success, true)
            .map(
                failure = { number: Int -> number + 2 },
                fallbackData = { number: Int? -> number?.plus(3) },
                data = { number -> number + 4 },
            ).shouldBeTypeOf<ResourceFailure<Int, Int>>()
            .left.shouldBe(Failure(failure + 2, success + 3, true))
    }

    @Test
    fun `Map SuccessLoading`() {
        buildResourceSuccessLoading<Int, Int>(success)
            .map(
                failure = { number: Int -> number + 2 },
                fallbackData = { number: Int? -> number?.plus(3) },
                data = { number -> number + 4 },
            ).shouldBeTypeOf<ResourceSuccessLoading<Int>>()
            .right.shouldBe(Success.Loading(success + 3))
    }

    @Test
    fun `Map SuccessData`() {
        buildResourceSuccessData<Int, Int>(success, true)
            .map(
                failure = { number: Int -> number + 2 },
                fallbackData = { number: Int? -> number?.plus(3) },
                data = { number -> number + 4 },
            ).shouldBeTypeOf<ResourceSuccessData<Int>>()
            .right.shouldBe(Success.Data(success + 4, true))
    }
}
