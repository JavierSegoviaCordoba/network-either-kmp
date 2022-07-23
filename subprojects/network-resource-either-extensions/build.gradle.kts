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
                        api(projects.subprojects.networkEither)
                        api(projects.subprojects.resourceEither)
                        api(ktorClientCore())
                    }
                }

                test {
                    dependencies {
                        implementation(ktorClientMock())
                    }
                }
            }

            jvm()
        }
    }
}
