hubdle {
    config {
        analysis()
        documentation {
            api()
        }
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
