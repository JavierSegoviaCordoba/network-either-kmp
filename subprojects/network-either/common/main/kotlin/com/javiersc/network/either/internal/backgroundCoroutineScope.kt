package com.javiersc.network.either.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

internal val backgroundCoroutineScope: CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Default)
