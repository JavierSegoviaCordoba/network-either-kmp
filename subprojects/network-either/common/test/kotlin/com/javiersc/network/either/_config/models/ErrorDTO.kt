package com.javiersc.network.either._config.models

import kotlinx.serialization.Serializable

@Serializable internal data class ErrorDTO(val message: String)

internal val errorDTO: ErrorDTO
    get() = ErrorDTO("Dog has some error")
