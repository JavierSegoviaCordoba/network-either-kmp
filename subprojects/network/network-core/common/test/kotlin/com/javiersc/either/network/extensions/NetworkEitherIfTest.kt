package com.javiersc.either.network.extensions

import com.javiersc.either.network.Failure
import com.javiersc.either.network.HttpStatusCode
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.NetworkFailureHttp
import com.javiersc.either.network.NetworkFailureLocal
import com.javiersc.either.network.NetworkFailureRemote
import com.javiersc.either.network.NetworkFailureUnknown
import com.javiersc.either.network.NetworkSuccess
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.buildNetworkFailureRemote
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.ktor._internal.emptyHeader
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.util.toMap
import kotlin.test.Test

class NetworkEitherIfTest {

    @Test
    fun `if Failure`() {
        val expected: NetworkEither<String, String> =
            buildNetworkFailureHttp("error", HttpStatusCode(400), emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifFailure { failure ->
            failure.shouldBeTypeOf<Failure.Http<String>>()
            with(failure) { actual = buildNetworkFailureHttp(error, code, headers) }
        }
        actual.shouldBeTypeOf<NetworkFailureHttp<String>>() shouldBe expected
    }

    @Test
    fun `if Failure Http`() {
        val expected: NetworkEither<String, String> =
            buildNetworkFailureHttp("error", HttpStatusCode(400), emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureHttp { error, code, headers ->
            actual = buildNetworkFailureHttp(error, code, headers)
        }
        actual.shouldBeTypeOf<NetworkFailureHttp<String>>() shouldBe expected
    }

    @Test
    fun `if Failure Http only error`() {
        val expected: NetworkEither<String, String> =
            buildNetworkFailureHttp("error", HttpStatusCode(400), emptyHeader.toMap())
        var actual: String? = null

        expected.ifFailureHttpOnlyError { error -> actual = error }
        actual shouldBe expected.shouldBeTypeOf<NetworkFailureHttp<String>>().left.error
    }

    @Test
    fun `if Failure Http only code`() {
        val expected: NetworkEither<String, String> =
            buildNetworkFailureHttp("error", HttpStatusCode(400), emptyHeader.toMap())
        var actual: HttpStatusCode? = null

        expected.ifFailureHttpOnlyCode { code -> actual = code }
        actual shouldBe expected.shouldBeTypeOf<NetworkFailureHttp<String>>().left.code
    }

    @Test
    fun `if Failure Local`() {
        val expected: NetworkEither<String, String> = buildNetworkFailureLocal()
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureLocal { actual = buildNetworkFailureLocal() }
        actual.shouldBeTypeOf<NetworkFailureLocal>() shouldBe expected
    }

    @Test
    fun `if Failure Remote`() {
        val expected: NetworkEither<String, String> = buildNetworkFailureRemote()
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureRemote { actual = buildNetworkFailureRemote() }
        actual.shouldBeTypeOf<NetworkFailureRemote>() shouldBe expected
    }

    @Test
    fun `if Failure Unknown`() {
        val expected: NetworkEither<String, String> =
            buildNetworkFailureUnknown(IllegalStateException("Error"))
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureUnknown { throwable -> actual = buildNetworkFailureUnknown(throwable) }
        actual.shouldBeTypeOf<NetworkFailureUnknown>() shouldBe expected
    }

    @Test
    fun `if Success`() {
        val expected: NetworkEither<String, String> =
            buildNetworkSuccess("success", HttpStatusCode(200), emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifSuccess { data, code, headers ->
            actual = buildNetworkSuccess(data, code, headers)
        }
        actual.shouldBeTypeOf<NetworkSuccess<String>>() shouldBe expected
    }

    @Test
    fun `if Success only data`() {
        val expected: NetworkEither<String, String> =
            buildNetworkSuccess("success", HttpStatusCode(200), emptyHeader.toMap())
        var actual: String? = null

        expected.ifSuccessOnlyData { data -> actual = data }
        actual shouldBe expected.shouldBeTypeOf<NetworkSuccess<String>>().right.data
    }
}
