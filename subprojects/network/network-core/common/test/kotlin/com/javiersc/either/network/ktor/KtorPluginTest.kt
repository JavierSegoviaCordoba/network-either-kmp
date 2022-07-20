package com.javiersc.either.network.ktor

import io.ktor.client.request.get

private val TODO: String
    get() =
        """
            |{
            |  "userId": 1,
            |  "id": 1,
            |  "title": "delectus aut autem",
            |  "completed": false
            |}
            |
        """.trimMargin()
