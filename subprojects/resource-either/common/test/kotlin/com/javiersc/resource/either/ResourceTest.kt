package com.javiersc.resource.either

import com.javiersc.resource.either.ResourceEither.Companion.buildResourceFailure
import com.javiersc.resource.either.ResourceEither.Companion.buildResourceFailureLoading
import com.javiersc.resource.either.ResourceEither.Companion.buildResourceSuccess
import com.javiersc.resource.either.ResourceEither.Companion.buildResourceSuccessLoading
import com.javiersc.resource.either.ResourceEither.Companion.resourceFailure
import com.javiersc.resource.either.ResourceEither.Companion.resourceSuccess
import com.javiersc.resource.either.ResourceEither.Companion.resourceSuccessLoading
import com.javiersc.resource.either.ResourceEither.Failure
import com.javiersc.resource.either.ResourceEither.Success
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class ResourceTest {

    private val fail = "Error"
    private val succs = "Success"

    @Test
    fun Create_Resource_Failure() {
        resourceFailure(fail, succs, true).shouldBe(Failure(fail, succs, true))
    }

    @Test
    fun Create_Resource_SuccessLoading() {
        resourceSuccessLoading(succs).shouldBe(Success(succs, true))
    }

    @Test
    fun Create_Resource_Success() {
        resourceSuccess(succs, true).shouldBe(Success(succs, true))
    }

    @Test
    fun Resource_isFailure() {
        val resource = resourceFailure(succs, null, true)
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
    fun Resource_isSuccess() {
        val resource = resourceSuccess(succs, true)
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
    fun Resource_successDataOrNull() {
        val resource: ResourceEither<Nothing, String> = resourceSuccess(succs, true)
        resource.successOrNull().shouldNotBeNull().data.shouldBe(succs)
        resource.successDataOrNull().shouldNotBeNull().shouldBe(succs)
    }

    @Test
    fun build_resource_failure() {
        val resource1: ResourceEither<String, String> =
            buildResourceFailure(failure = fail, data = succs, isLoading = false)
        resource1.shouldBeInstanceOf<Failure<String, String>>().apply {
            this.failure.shouldBe(fail)
            this.isLoading.shouldBe(false)
            this.data.shouldBe(succs)
        }

        val resource2: ResourceEither<String, String> =
            buildResourceFailureLoading(failure = fail, data = succs)
        resource2.shouldBeInstanceOf<Failure<String, String>>().apply {
            this.failure.shouldBe(fail)
            this.isLoading.shouldBe(true)
            this.data.shouldBe(succs)
        }
    }

    @Test
    fun build_resource_success() {
        val resource1: ResourceEither<String, String> =
            buildResourceSuccess(data = succs, isLoading = false)
        resource1.shouldBeInstanceOf<Success<String>>().apply {
            this.isLoading.shouldBe(false)
            this.data.shouldBe(succs)
        }

        val resource2: ResourceEither<String, String> = buildResourceSuccessLoading(data = succs)
        resource2.shouldBeInstanceOf<Success<String>>().apply {
            this.isLoading.shouldBe(true)
            this.data.shouldBe(succs)
        }
    }
}
