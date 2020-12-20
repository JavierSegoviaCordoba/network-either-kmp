package com.javiersc.either.resource

import com.javiersc.either.resource.extensions.fold
import com.javiersc.either.resource.extensions.ifFailure
import com.javiersc.either.resource.extensions.ifSuccessData
import com.javiersc.either.resource.extensions.ifSuccessLoading
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class ResourceFoldTest {

    private val failure = 2
    private val success = 4

    @Test
    fun `Fold Failure`() {
        var actual = 0
        var counter = 0
        var actualIsLoading = false

        val resource = buildResourceFailure(failure, success, true)

        resource.fold(
            failure = { failure, data, isLoading ->
                actual = failure + data!!
                actualIsLoading = isLoading
                counter++
            },
            data = { data, isLoading ->
                actual = data
                actualIsLoading = isLoading
                counter++
            },
            loading = {
                actual = it!!
                actualIsLoading = true
                counter++
            },
        )

        actual shouldBe 6
        counter shouldBe 1
        actualIsLoading shouldBe true

        actualIsLoading = false

        resource.ifFailure { failure, data, isLoading ->
            actual = failure + data!!
            actualIsLoading = isLoading
            counter++
        }

        actual shouldBe 6
        counter shouldBe 2
        actualIsLoading shouldBe true
    }

    @Test
    fun `Fold SuccessLoading`() {
        var actual = 0
        var counter = 0
        var actualIsLoading = false

        val resource = buildResourceSuccessLoading<Int, Int>(success)

        resource.fold(
            failure = { failure, data, isLoading ->
                actual = failure + data!!
                actualIsLoading = isLoading
                counter++
            },
            data = { data, isLoading ->
                actual = data
                actualIsLoading = isLoading
                counter++
            },
            loading = {
                actual = it!!
                actualIsLoading = true
                counter++
            },
        )

        actual shouldBe 4
        counter shouldBe 1
        actualIsLoading shouldBe true

        actualIsLoading = false

        resource.ifSuccessLoading { data ->
            actual = data!!
            actualIsLoading = true
            counter++
        }

        actual shouldBe 4
        counter shouldBe 2
        actualIsLoading shouldBe true
    }

    @Test
    fun `Fold SuccessData`() {
        var actual = 0
        var counter = 0
        var actualIsLoading = false

        val resource = buildResourceSuccessData<Int, Int>(success, true)

        resource.fold(
            failure = { failure, data, isLoading ->
                actual = failure + data!!
                actualIsLoading = isLoading
                counter++
            },
            data = { data, isLoading ->
                actual = data
                actualIsLoading = isLoading
                counter++
            },
            loading = {
                actual = it!!
                actualIsLoading = true
                counter++
            },
        )

        actual shouldBe 4
        counter shouldBe 1
        actualIsLoading shouldBe true

        actualIsLoading = false

        resource.ifSuccessData { data ->
            actual = data
            actualIsLoading = true
            counter++
        }

        actual shouldBe 4
        counter shouldBe 2
        actualIsLoading shouldBe true
    }
}
