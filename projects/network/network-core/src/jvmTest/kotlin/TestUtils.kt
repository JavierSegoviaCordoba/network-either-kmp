package com.javiersc.either.network

import kotlinx.coroutines.runBlocking

internal actual fun Any.readResource(file: String): String =
    this::class.java.classLoader.getResource(file)!!.readText()

internal actual fun runTestBlocking(block: suspend () -> Unit): Unit = runBlocking { block() }
