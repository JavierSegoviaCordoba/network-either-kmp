package com.javiersc.either.network

internal expect fun Any.readResource(file: String): String

internal expect fun runTestBlocking(block: suspend () -> Unit)
