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
                        api(projects.subprojects.eitherCore)
                        api(projects.subprojects.network.networkCore)
//                        api(projects.subprojects.resourceCore)
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
