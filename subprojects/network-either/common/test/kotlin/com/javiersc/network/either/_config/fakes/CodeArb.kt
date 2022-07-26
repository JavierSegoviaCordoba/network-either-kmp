package com.javiersc.network.either._config.fakes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int

internal val successCodes: Arb<Int> = Arb.int(200, 299)

internal val clientErrorCodes: Arb<Int> = Arb.int(400, 499).filter { it != 407 }

internal val serverErrorCodes: Arb<Int> = Arb.int(500, 599)
