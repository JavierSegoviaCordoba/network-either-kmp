package com.javiersc.either.network

import kotlinx.coroutines.runBlocking
import okio.ExperimentalFileSystem
import okio.FileSystem

@OptIn(ExperimentalFileSystem::class) internal actual val fileSystem: FileSystem = FileSystem.SYSTEM

internal actual fun runTestBlocking(block: suspend () -> Unit): Unit = runBlocking { block() }
