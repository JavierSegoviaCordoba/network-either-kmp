hubdle {
    config {
        analysis()
        coverage()
        documentation { //
            api()
        }
        explicitApi()
        languageSettings {
            experimentalContracts()
            experimentalCoroutinesApi()
            experimentalSerializationApi()
        }
        publishing()
    }
    kotlin {
        multiplatform {
            features {
                coroutines()
                kotest()
                serialization()
            }

            common {
                main {
                    dependencies {
                        api(hubdle.ktor.client.core)
                        api(hubdle.ktor.client.content.negotiation)
                        api(hubdle.ktor.serialization.kotlinx.json)
                    }
                }

                test {
                    dependencies {
                        implementation(hubdle.ktor.client.mock)
                        implementation(hubdle.kotest.property)
                    }
                }
            }

            apple {
                main {
                    dependencies {
                        api(hubdle.ktor.client.darwin)
                    }
                }

                ios {
                    iosArm64()
                    iosSimulatorArm64()
                    iosX64()
                }
                macos {
                    macosArm64()
                    macosX64()
                }
                tvos {
                    tvosArm64()
                    tvosSimulatorArm64()
                    tvosX64()
                }
                watchos {
                    watchosArm64()
                    watchosSimulatorArm64()
                    watchosX64()
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
                    }
                }
            }

            js {
                // browser()
                nodejs()
            }

            linux {
                // linuxArm64()
                // linuxX64 {
                //     main {
                //         dependencies { //
                //             api(hubdle.ktor.client.curl)
                //         }
                //     }
                // }
            }

            mingw {
                mingwX64 {
                    main {
                        dependencies { //
                            api(hubdle.ktor.client.winhttp)
                        }
                    }
                }
            }

            native()

            wasm {
                js {
                    browser()
                    // d8()
                    nodejs()
                }
                // wasi {
                //     nodejs()
                // }
            }
        }
    }
}
