package com.javiersc.network.either._config.fakes

internal val Json2xx: String =
    """
        {
          "id": 1,
          "name": "Auri",
          "age": 7
        }
    """
        .trimIndent()

internal val Json4xx: String =
    """
        {
          "message": "Dog has some error"
        }
    """
        .trimIndent()

internal val Json5xx: String =
    """
        {
          "message": "Dog has some error"
        }
    """
        .trimIndent()

internal val Json6xx: String =
    """
        {
          "message": "Dog has some error"
        }
    """
        .trimIndent()

internal val JsonMalformed: String =
    """
        {
          "id": 1,
          "name": "Auri",
          "age": 7
    """
        .trimIndent()

internal fun getJsonResponse(code: Int): String =
    when (code) {
        in 200..299 -> Json2xx
        in 400..499 -> Json4xx
        in 500..599 -> Json5xx
        in 600..699 -> Json6xx
        else -> error("Unhandled code: $code")
    }
