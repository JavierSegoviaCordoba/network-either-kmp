package com.javiersc.network.either._config

import okio.Path
import okio.Path.Companion.toPath

internal fun readResource(file: String): String =
    fileSystem.read("$resourcesFolderAbsolutePath/$file".toPath()) { readUtf8() }

private val resourcesFolderAbsolutePath: Path
    get() = fileSystem.canonicalize("./common/test/resources".toPath())
