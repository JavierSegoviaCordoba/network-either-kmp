package com.javiersc.either.network.utils

import com.javiersc.either.network.internal.Constants
import java.net.InetSocketAddress
import java.net.Socket

public actual val isNetworkAvailable: Boolean
    get() = runCatching {
        Socket().apply {
            connect(InetSocketAddress(Constants.DnsIp, Constants.DnsPort), Constants.DnsTimeout)
            close()
        }
    }.isSuccess
