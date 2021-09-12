package com.javiersc.either.network

import okio.ExperimentalFileSystem
import okio.FileSystem

@OptIn(ExperimentalFileSystem::class) internal actual val fileSystem: FileSystem = FileSystem.SYSTEM
