package com.javiersc.resource.either

import arrow.core.Either
import com.javiersc.resource.either.ResourceEither.Companion.resourceFailure
import com.javiersc.resource.either.ResourceEither.Companion.resourceSuccess
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ResourceFoldTest {

    private val failure = 2
    private val success = 4

    @Test
    fun Fold_Failure() {
        var actual = 0
        var counter = 0
        var actualIsLoading = false

        val resource: ResourceEither<Int, Int> = resourceFailure(failure, success, true)

        resource
            .fold(
                failure = { (failure, data, isLoading) ->
                    actual = failure + data!!
                    actualIsLoading = isLoading
                    counter++
                    "FAILURE"
                },
                success = { (data, isLoading) ->
                    actual = data
                    actualIsLoading = isLoading
                    counter++
                    "SUCCESS"
                },
            )
            .shouldBe("FAILURE")

        actual shouldBe 6
        counter shouldBe 1
        actualIsLoading shouldBe true

        actualIsLoading = false

        resource.ifFailure {
            actual = it.failure + it.data!!
            actualIsLoading = it.isLoading
            counter++
        }

        resource.ifSuccess {
            actual = it.data
            actualIsLoading = it.isLoading
            counter++
        }

        actual shouldBe 6
        counter shouldBe 2
        actualIsLoading shouldBe true
    }

    @Test
    fun Fold_Success() {
        var actual = 0
        var counter = 0
        var actualIsLoading = false

        val resource: ResourceEither<Int, Int> = resourceSuccess(success, true)

        resource
            .fold(
                failure = { (failure, data, isLoading) ->
                    actual = failure + data!!
                    actualIsLoading = isLoading
                    counter++
                    "FAILURE"
                },
                success = { (data, isLoading) ->
                    actual = data
                    actualIsLoading = isLoading
                    counter++
                    "SUCCESS"
                },
            )
            .shouldBe("SUCCESS")

        actual shouldBe 4
        counter shouldBe 1
        actualIsLoading shouldBe true

        actualIsLoading = false

        resource.ifFailure {
            actual = it.failure + it.data!!
            actualIsLoading = it.isLoading
            counter++
        }

        resource.ifSuccess { (data, isLoading) ->
            actual = data
            actualIsLoading = isLoading
            counter++
        }

        actual shouldBe 4
        counter shouldBe 2
        actualIsLoading shouldBe true
    }

    @Test
    fun Resource_to_Either() {
        val failureResource: ResourceEither<Int, Int> = resourceFailure(success, failure, true)

        failureResource
            .toEither(failure = { "ERROR" }, success = { "SUCCESS" })
            .shouldBe(Either.Left("ERROR"))

        val successResource: ResourceEither<Int, Int> = resourceSuccess(success, true)

        successResource
            .toEither(failure = { "ERROR" }, success = { "SUCCESS" })
            .shouldBe(Either.Right("SUCCESS"))
    }
}
