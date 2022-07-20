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
                        api(projects.subprojects.eitherCore)
                    }
                }
            }

            jvm()
        }
    }
}
