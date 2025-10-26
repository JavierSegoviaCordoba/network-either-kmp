hubdle {
    config {
        analysis()
        coverage()
        documentation { //
            api()
        }
        explicitApi()
        publishing()
    }
    kotlin {
        multiplatform {
            features {
                coroutines()
                jvmVersion(JavaVersion.VERSION_11)
                kotest()
                serialization()
            }

            common {
                main {
                    dependencies {
                        api(projects.subprojects.networkEither)
                        api(projects.subprojects.resourceEither)
                        api(hubdle.ktor.client.core)
                    }
                }

                test {
                    dependencies { //
                        implementation(hubdle.ktor.client.mock)
                    }
                }
            }

            jvm()

            js {
                // browser()
                nodejs()
            }

            linux {
                // linuxArm64()
                // linuxX64()
            }

            mingw { //
                mingwX64()
            }

            native()
        }
    }
}
