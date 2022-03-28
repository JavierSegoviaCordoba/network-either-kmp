package com.javiersc.either

import com.javiersc.either.extensions.fold
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class EitherFoldTest {

    @Test
    fun fold_left() {
        var left = 0
        var right = 0
        val either: Either<Int, Int> = Either.Left(1)
        either.fold(
            left = { left += it },
            right = { right += it },
        )
        left shouldBe 1
        right shouldBe 0
    }

    @Test
    fun fold_right() {
        var left = 0
        var right = 0
        val either: Either<Int, Int> = Either.Right(1)
        either.fold(
            left = { left += it },
            right = { right += it },
        )
        left shouldBe 0
        right shouldBe 1
    }
}
