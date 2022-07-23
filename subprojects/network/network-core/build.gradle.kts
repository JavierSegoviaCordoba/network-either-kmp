plugins { alias(libs.plugins.javiersc.hubdle) }

hubdle {
    config {
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
                        implementation(squareupOkhttp3Mockwebserver())
                        implementation(squareupOkhttp3Mockwebserver3Junit4())
                    }
                }
            }
        }
    }
}

tasks.withType<Test>().configureEach { maxParallelForks = 1 }
