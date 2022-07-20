plugins {
    alias(libs.plugins.javiersc.hubdle)
}

hubdle {
    config {
        explicitApi()
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
                        api(projects.subprojects.eitherCore)
                        implementation(javierscKotlinxCoroutinesRunBlocking())
                        api(ktorClientContentNegotiation())
                        api(ktorClientCore())
                        api(ktorSerializationKotlinxJson())
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
                    }
                }
            }
        }
    }
}
