package com.javiersc.either.network

import okio.Path.Companion.toPath

val resourcesFolderAbsolutePath = fileSystem.canonicalize("./common/test/resources".toPath())

internal fun readResource(file: String): String =
    fileSystem.read("$resourcesFolderAbsolutePath/$file".toPath()) { readUtf8() }
