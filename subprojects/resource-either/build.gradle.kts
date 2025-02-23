hubdle {
    config {
        analysis()
        coverage()
        documentation { //
            api()
        }
        explicitApi()
        languageSettings { //
            experimentalContracts()
        }
        publishing()
    }
    kotlin {
        multiplatform {
            features { //
                kotest()
            }
            common {
                main {
                    dependencies { //
                        implementation(hubdle.arrow.kt.arrow.core)
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
