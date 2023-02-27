plugins {
    alias(libs.plugins.javiersc.hubdle)
}

hubdle {
    config {
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
                        api(arrowKtArrowCore())
                        api(ktorClientContentNegotiation())
                        api(ktorClientCore())
                        api(ktorSerializationKotlinxJson())
                        implementation(javierscKotlinxCoroutinesRunBlocking())
                    }
                }

                test {
                    dependencies {
                        implementation(ktorClientMock())
                        implementation(squareupOkio())
                    }
                }
            }

            jvm {
                main {
                    dependencies {
                        api(ktorClientCio())
                        api(squareupOkhttp3Okhttp())
                        api(squareupRetrofit2Retrofit())
                    }
                }

                test {
                    dependencies {
                        implementation(jakewhartonRetrofit2KotlinxSerializationConverter())
                        implementation(kotestExtensionsMockserver())
                        implementation(kotestProperty())
                    }
                }
            }
        }
    }
}
