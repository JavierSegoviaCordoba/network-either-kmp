package com.javiersc.either.store.internal

import com.javiersc.either.store.StoreException

@Throws(exceptionClasses = [StoreException::class])
internal fun storeError(message: String): Nothing {
    throw StoreException(message)
}
