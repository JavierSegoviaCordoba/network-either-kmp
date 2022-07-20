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
            common {
                main {
                    dependencies {
                        implementation(projects.subprojects.network.networkCore)
                        api(javierscMokokiCore())
                        api(javierscMokokiSerialization())
                    }
                }
            }

            jvm()
        }
    }
}
