package com.javiersc.either.network

import okio.ExperimentalFileSystem
import okio.FileSystem

@OptIn(ExperimentalFileSystem::class) internal expect val fileSystem: FileSystem

internal expect fun runTestBlocking(block: suspend () -> Unit)
