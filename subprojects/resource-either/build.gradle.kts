plugins {
    alias(libs.plugins.javiersc.hubdle)
}

hubdle {
    config {
        explicitApi()
        languageSettings {
            experimentalContracts()
        }
        publishing()
    }
    kotlin {
        multiplatform {
            common {
                main {
                    dependencies {
                        implementation(arrowKtArrowCore())
                    }
                }
            }

            jvm()
        }
    }
}
