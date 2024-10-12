package com.javiersc.resource.either

import com.javiersc.resource.either.extensions.asFailure
import com.javiersc.resource.either.extensions.asSuccess
import com.javiersc.resource.either.extensions.asSuccessLoading
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class ResourceAsTest {

    @Test
    fun as_Failure() {
        val foo: ResourceEither<Int, Int> = 2.asFailure()
        foo.shouldBeInstanceOf<ResourceEither.Failure<Int, Int>>().apply {
            this.failure shouldBe 2
            this.isLoading shouldBe false
            this.data shouldBe null
        }
        val foo2: ResourceEither<Int, Int> = 2.asFailure(data = 3, isLoading = true)
        foo2.shouldBeInstanceOf<ResourceEither.Failure<Int, Int>>().apply {
            this.failure shouldBe 2
            this.isLoading shouldBe true
            this.data shouldBe 3
        }
    }

    @Test
    fun as_Success_Loading() {
        val foo: ResourceEither<Int, Int> = 2.asSuccessLoading()
        foo.shouldBeInstanceOf<ResourceEither.Success<*>>().isLoading.shouldBeTrue()
    }

    @Test
    fun asSuccess() {
        val foo: ResourceEither<Int, Int> = 2.asSuccess()
        foo.shouldBeInstanceOf<ResourceEither.Success<*>>().apply {
            this.isLoading.shouldBeFalse()
            this.data shouldBe 2
        }
        val foo2: ResourceEither<Int, Int> = 2.asSuccess(isLoading = true)
        foo2.shouldBeInstanceOf<ResourceEither.Success<*>>().apply {
            this.isLoading.shouldBeTrue()
            this.data shouldBe 2
        }
    }
}
