@file:Suppress("PropertyName", "VariableNaming")

// Catalog name: libs

// [versions]
val coroutines = "1.5.1-native-mt"
val kotest = "4.6.1"
val ktor = "1.6.2"
val mokoki = "0.1.0-alpha.5"
val okHttp = "4.9.1"
val okio = "3.0.0-alpha.9"
val retrofit = "2.9.0"
val retrofitKotlinConverter = "0.8.0"
val runBlocking = "0.1.0-alpha.4"
val serialization = "1.2.2"

// [libraries]
val jakewharton_retrofit_retrofit2KotlinxSerializationConverter =
    "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:$retrofitKotlinConverter"
val javiersc_mokoki_mokokiCore = "com.javiersc.mokoki:mokoki-core:$mokoki"
val javiersc_mokoki_mokokiSerialization = "com.javiersc.mokoki:mokoki-serialization:$mokoki"
val javiersc_runBlocking_runBlockingCore =
    "com.javiersc.run-blocking:run-blocking-core:$runBlocking"
val javiersc_runBlocking_suspendTest = "com.javiersc.run-blocking:suspend-test:$runBlocking"
val jetbrains_kotlin_kotlinTestCommon = "org.jetbrains.kotlin:kotlin-test-common"
val jetbrains_kotlin_kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit"
val jetbrains_kotlinx_kotlinxCoroutinesCore =
    "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines"
val jetbrains_kotlinx_kotlinxSerializationJson =
    "org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization"
val kotest_kotestAssertionsCore = "io.kotest:kotest-assertions-core:$kotest"
val ktor_ktorClientCio = "io.ktor:ktor-client-cio:$ktor"
val ktor_ktorClientCore = "io.ktor:ktor-client-core:$ktor"
val ktor_ktorClientMock = "io.ktor:ktor-client-mock:$ktor"
val ktor_ktorClientSerialization = "io.ktor:ktor-client-serialization:$ktor"
val squareup_okhttp3_okhttp = "com.squareup.okhttp3:okhttp:$okHttp"
val squareup_okhttp3_mockwebserver = "com.squareup.okhttp3:mockwebserver:$okHttp"
val squareup_okio_okio = "com.squareup.okio:okio:$okio"
val squareup_retrofit2_retrofit = "com.squareup.retrofit2:retrofit:$retrofit"

// [bundles]
val testing = jetbrains_kotlin_kotlinTestCommon + kotest_kotestAssertionsCore
