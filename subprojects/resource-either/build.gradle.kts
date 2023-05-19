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
        }
        publishing()
    }
    kotlin {
        multiplatform {
            common {
                main {
                    dependencies {
                        implementation(hubdle.arrowKt.arrowCore)
                    }
                }
            }

            jvm()
        }
    }
}
