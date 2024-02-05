hubdle {
    config {
        analysis()
        documentation {
            api()
        }
        explicitApi()
        publishing()
    }
    kotlin {
        multiplatform {
            features {
                serialization()
            }

            common {
                main {
                    dependencies {
                        implementation(projects.subprojects.networkEither)
                        api(hubdle.javiersc.mokoki.serialization)
                    }
                }
            }

            jvm()
        }
    }
}
