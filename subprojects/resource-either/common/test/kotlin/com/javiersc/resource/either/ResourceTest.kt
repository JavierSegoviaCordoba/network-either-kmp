package com.javiersc.resource.either

import com.javiersc.resource.either.ResourceEither.Companion.resourceFailure
import com.javiersc.resource.either.ResourceEither.Companion.resourceSuccess
import com.javiersc.resource.either.ResourceEither.Companion.resourceSuccessLoading
import com.javiersc.resource.either.ResourceEither.Failure
import com.javiersc.resource.either.ResourceEither.Success
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class ResourceTest {
    private val failure = "Error"
    private val success = "Success"

    @Test
    fun `Create Resource Failure`() {
        resourceFailure(failure, success, true).shouldBe(Failure(failure, success, true))
    }

    @Test
    fun `Create Resource SuccessLoading`() {
        resourceSuccessLoading(success).shouldBe(Success(success, true))
    }

    @Test
    fun `Create Resource Success`() {
        resourceSuccess(success, true).shouldBe(Success(success, true))
    }

    @Test
    fun `Resource isFailure`() {
        val resource = resourceFailure(success, null, true)
        var counter = 0
        if (resource.isLoading()) {
            counter++
        }
        if (resource.isFailure()) {
            counter++
        }
        if (resource.isSuccess()) {
            counter++
        }
        counter.shouldBe(2)
    }

    @Test
    fun `Resource isSuccess`() {
        val resource = resourceSuccess(success, true)
        var counter = 0
        if (resource.isLoading()) {
            counter++
        }
        if (resource.isFailure()) {
            counter++
        }
        if (resource.isSuccess()) {
            counter++
        }
        counter.shouldBe(2)
    }
}
