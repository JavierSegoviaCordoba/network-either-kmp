hubdle {
    config {
        analysis()
        coverage()
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
                        api(hubdle.ktor.client.core)
                    }
                }

                test {
                    dependencies {
                        implementation(hubdle.ktor.client.mock)
                    }
                }
            }

            jvm()
        }
    }
}
