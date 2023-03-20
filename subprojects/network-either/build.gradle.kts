hubdle {
    config {
        analysis()
        documentation {
            api()
        }
        explicitApi()
        languageSettings {
            experimentalContracts()
            experimentalCoroutinesApi()
            experimentalSerializationApi()
            optIn("io.ktor.util.InternalAPI")
        }
        publishing()
    }
    kotlin {
        multiplatform {
            features {
                coroutines()
                serialization()
            }

            common {
                main {
                    dependencies {
                        api(hubdle.arrowKt.arrowCore)
                        api(hubdle.ktor.ktorClientCore)
                        api(hubdle.ktor.ktorClientContentNegotiation)
                        api(hubdle.ktor.ktorSerializationKotlinxJson)
                        implementation(hubdle.javiersc.kotlinx.coroutinesRunBlocking)
                    }
                }

                test {
                    dependencies {
                        implementation(hubdle.ktor.ktorClientMock)
                        implementation(hubdle.squareup.okio.okio)
                    }
                }
            }

            jvm {
                main {
                    dependencies {
                        api(hubdle.ktor.ktorClientCio)
                        api(hubdle.squareup.okhttp3.okhttp)
                        api(hubdle.squareup.retrofit2.retrofit)
                    }
                }

                test {
                    dependencies {
                        implementation(hubdle.squareup.retrofit2.converterKotlinxSerialization)
                        implementation(hubdle.kotest.extensions.kotestExtensionsMockserver)
                        implementation(hubdle.kotest.kotestProperty)
                    }
                }
            }
        }
    }
}
