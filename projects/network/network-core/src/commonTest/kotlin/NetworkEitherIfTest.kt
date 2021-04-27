package com.javiersc.either.network

import com.javiersc.either.network.extensions.ifFailureHttp
import com.javiersc.either.network.extensions.ifFailureLocal
import com.javiersc.either.network.extensions.ifFailureRemote
import com.javiersc.either.network.extensions.ifFailureUnknown
import com.javiersc.either.network.extensions.ifSuccess
import com.javiersc.either.network.ktor.emptyHeader
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.util.toMap
import kotlin.test.Test

class NetworkEitherIfTest {

    @Test
    fun `if Failure Http`() {
        val expected: NetworkEither<String, String> = buildNetworkFailureHttp("error", 400, emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null
        expected.ifFailureHttp { error, code, headers ->
            actual = buildNetworkFailureHttp(error, code, headers)
        }
        actual.shouldBeTypeOf<NetworkFailureHttp<String>>() shouldBe expected
    }

    @Test
    fun `if Failure Local`() {
        val expected: NetworkEither<String, String> = buildNetworkFailureLocal()
        var actual: NetworkEither<String, String>? = null
        expected.ifFailureLocal {
            actual = buildNetworkFailureLocal()
        }
        actual.shouldBeTypeOf<NetworkFailureLocal>() shouldBe expected
    }

    @Test
    fun `if Failure Remote`() {
        val expected: NetworkEither<String, String> = buildNetworkFailureRemote()
        var actual: NetworkEither<String, String>? = null
        expected.ifFailureRemote {
            actual = buildNetworkFailureRemote()
        }
        actual.shouldBeTypeOf<NetworkFailureRemote>() shouldBe expected
    }

    @Test
    fun `if Failure Unknown`() {
        val expected: NetworkEither<String, String> = buildNetworkFailureUnknown(IllegalStateException("Error"))
        var actual: NetworkEither<String, String>? = null
        expected.ifFailureUnknown { throwable ->
            actual = buildNetworkFailureUnknown(throwable)
        }
        actual.shouldBeTypeOf<NetworkFailureUnknown>() shouldBe expected
    }

    @Test
    fun `if Success`() {
        val expected: NetworkEither<String, String> = buildNetworkSuccess("success", 200, emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null
        expected.ifSuccess { data, code, headers ->
            actual = buildNetworkSuccess(data, code, headers)
        }
        actual.shouldBeTypeOf<NetworkSuccess<String>>() shouldBe expected
    }
}
