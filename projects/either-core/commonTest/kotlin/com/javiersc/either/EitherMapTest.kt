package com.javiersc.either

import com.javiersc.either.extensions.map
import com.javiersc.either.extensions.mapLeft
import com.javiersc.either.extensions.mapRight
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlin.test.Test

class EitherMapTest {

    @Test
    fun left_mappers() {
        val either: Either<Int, String> = Either.Left(1)
        either
            .map(left = { it + 1 }, right = { "" })
            .shouldBeTypeOf<Either.Left<Int>>()
            .left shouldBe 2

        either.mapLeft { it + 2 }.shouldBeTypeOf<Either.Left<Int>>().left shouldBe 3
    }

    @Test
    fun right_mappers() {
        val either: Either<String, Int> = Either.Right(1)
        either
            .map(left = { "" }, right = { it + 1 })
            .shouldBeTypeOf<Either.Right<Int>>()
            .right shouldBe 2

        either.mapRight { it + 2 }.shouldBeTypeOf<Either.Right<Int>>().right shouldBe 3
    }
}
