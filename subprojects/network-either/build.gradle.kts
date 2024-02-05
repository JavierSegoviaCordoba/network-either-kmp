hubdle {
    config {
        analysis()
        coverage()
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
                        api(hubdle.arrow.kt.arrow.core)
                        api(hubdle.ktor.client.core)
                        api(hubdle.ktor.client.content.negotiation)
                        api(hubdle.ktor.serialization.kotlinx.json)
                        implementation(hubdle.javiersc.kotlinx.coroutines.run.blocking)
                    }
                }

                test {
                    dependencies {
                        implementation(hubdle.ktor.client.mock)
                        implementation(hubdle.squareup.okio)
                    }
                }
            }

            jvm {
                main {
                    dependencies {
                        api(hubdle.ktor.client.cio)
                        api(hubdle.squareup.okhttp3.okhttp)
                        api(hubdle.squareup.retrofit2.retrofit)
                    }
                }

                test {
                    dependencies {
                        implementation(hubdle.squareup.retrofit2.converter.kotlinx.serialization)
                        implementation(hubdle.kotest.extensions.kotest.extensions.mockserver)
                        implementation(hubdle.kotest.property)
                    }
                }
            }
        }
    }
}
