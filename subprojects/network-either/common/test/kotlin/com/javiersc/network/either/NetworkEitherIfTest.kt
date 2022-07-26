package com.javiersc.network.either

import com.javiersc.network.either.ktor._internal.emptyHeader
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.util.toMap
import kotlin.test.Test

class NetworkEitherIfTest {

    @Test
    fun `if Failure`() {
        val expected: NetworkEither<String, String> =
            NetworkEither.httpFailure("error", 400, emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifFailure { failure ->
            failure.shouldBeTypeOf<NetworkEither.Failure.Http<String>>()
            with(failure) { actual = NetworkEither.httpFailure(error, code, headers) }
        }
        actual.shouldBeTypeOf<NetworkFailureHttp<String>>() shouldBe expected
    }

    @Test
    fun `if Failure Http`() {
        val expected: NetworkEither<String, String> =
            NetworkEither.httpFailure("error", 400, emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureHttp { (error, code, headers) ->
            actual = NetworkEither.httpFailure(error, code, headers)
        }
        actual.shouldBeTypeOf<NetworkFailureHttp<String>>() shouldBe expected
    }

    @Test
    fun `if Failure Local`() {
        val expected: NetworkEither<String, String> = NetworkEither.localFailure()
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureLocal { actual = NetworkEither.localFailure() }
        actual.shouldBeTypeOf<NetworkFailureLocal>() shouldBe expected
    }

    @Test
    fun `if Failure Remote`() {
        val expected: NetworkEither<String, String> = NetworkEither.remoteFailure()
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureRemote { actual = NetworkEither.remoteFailure() }
        actual.shouldBeTypeOf<NetworkFailureRemote>() shouldBe expected
    }

    @Test
    fun `if Failure Unknown`() {
        val expected: NetworkEither<String, String> =
            NetworkEither.unknownFailure(IllegalStateException("Error"))
        var actual: NetworkEither<String, String>? = null

        expected.ifFailureUnknown { throwable -> actual = NetworkEither.unknownFailure(throwable) }
        actual.shouldBeTypeOf<NetworkFailureUnknown>() shouldBe expected
    }

    @Test
    fun `if Success`() {
        val expected: NetworkEither<String, String> =
            NetworkEither.success("success", 200, emptyHeader.toMap())
        var actual: NetworkEither<String, String>? = null

        expected.ifSuccess { (data, code, headers) ->
            actual = NetworkEither.success(data, code, headers)
        }
        actual.shouldBeTypeOf<NetworkSuccess<String>>() shouldBe expected
    }
}
