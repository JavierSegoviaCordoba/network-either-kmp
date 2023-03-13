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
