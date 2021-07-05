package com.javiersc.either.network

import okio.ExperimentalFileSystem
import okio.Path.Companion.toPath

@OptIn(ExperimentalFileSystem::class)
val resourcesFolderAbsolutePath = fileSystem.canonicalize("./src/commonTest/resources".toPath())

@OptIn(ExperimentalFileSystem::class)
internal fun readResource(file: String): String =
    fileSystem.read("$resourcesFolderAbsolutePath/$file".toPath()) { readUtf8() }
