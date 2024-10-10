package com.javiersc.network.either._config

import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString

internal fun readResource(file: String): String =
    SystemFileSystem.source(Path(resourcesFolderAbsolutePath, file))
        .buffered()
        .use(Source::readString)

private val resourcesFolderAbsolutePath: Path
    get() = SystemFileSystem.resolve(Path(path = "./common/test/resources"))
