package com.javiersc.network.either.extensions

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.httpFailure
import com.javiersc.network.either.NetworkEither.Companion.localFailure
import com.javiersc.network.either.NetworkEither.Companion.remoteFailure
import com.javiersc.network.either.NetworkEither.Companion.success
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import com.javiersc.network.either.NetworkFailureHttp
import com.javiersc.network.either.NetworkFailureLocal
import com.javiersc.network.either.NetworkFailureRemote
import com.javiersc.network.either.NetworkFailureUnknown
import com.javiersc.network.either.NetworkSuccess
import com.javiersc.network.either.ktor._internal.emptyHeader
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.util.toMap
import kotlin.test.Test

class NetworkEitherIfTest {

    @Test
    fun `if Failure`() {
        val expected: NetworkEither<String, String> = httpFailure("error", 400, emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifFailure { failure ->
            failure.shouldBeTypeOf<NetworkEither.Failure.Http<String>>()
            with(failure) { actual = httpFailure(error, code, headers) }
        }
        actual.shouldBeTypeOf<NetworkFailureHttp<String>>() shouldBe expected
    }

    @Test
    fun `if Failure Http`() {
        val expected: NetworkEither<String, String> = httpFailure("error", 400, emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureHttp { (error, code, headers) ->
            actual = httpFailure(error, code, headers)
        }
        actual.shouldBeTypeOf<NetworkFailureHttp<String>>() shouldBe expected
    }

    @Test
    fun `if Failure Local`() {
        val expected: NetworkEither<String, String> = localFailure()
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureLocal { actual = localFailure() }
        actual.shouldBeTypeOf<NetworkFailureLocal>() shouldBe expected
    }

    @Test
    fun `if Failure Remote`() {
        val expected: NetworkEither<String, String> = remoteFailure()
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureRemote { actual = remoteFailure() }
        actual.shouldBeTypeOf<NetworkFailureRemote>() shouldBe expected
    }

    @Test
    fun `if Failure Unknown`() {
        val expected: NetworkEither<String, String> = unknownFailure(IllegalStateException("Error"))
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureUnknown { throwable -> actual = unknownFailure(throwable) }
        actual.shouldBeTypeOf<NetworkFailureUnknown>() shouldBe expected
    }

    @Test
    fun `if Success`() {
        val expected: NetworkEither<String, String> = success("success", 200, emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifSuccess { (data, code, headers) -> actual = success(data, code, headers) }
        actual.shouldBeTypeOf<NetworkSuccess<String>>() shouldBe expected
    }
}
