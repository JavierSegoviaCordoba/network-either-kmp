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
                        api(hubdle.arrow.kt.arrow.core)
                        api(projects.subprojects.networkEither)
                    }
                }
            }

            apple {
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

            jvm()

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
                mingwX64()
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
