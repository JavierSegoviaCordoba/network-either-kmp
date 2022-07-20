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
                serialization()
            }

            common {
                main {
                    dependencies {
                        implementation(projects.subprojects.network.networkCore)
                        api(javierscMokokiSerialization())
                    }
                }
            }

            jvm()
        }
    }
}
