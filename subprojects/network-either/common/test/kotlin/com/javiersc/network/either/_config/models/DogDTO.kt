package com.javiersc.network.either._config.models

import kotlinx.serialization.Serializable

@Serializable internal data class DogDTO(val id: Int, val name: String, val age: Int)

internal val dogDTO: DogDTO
    get() = DogDTO(1, "Auri", 7)
