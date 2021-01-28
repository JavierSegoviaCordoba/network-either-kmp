package com.javiersc.either.network.utils

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope

public actual fun <T> runBlocking(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): T = kotlinx.coroutines.runBlocking(context, block)
