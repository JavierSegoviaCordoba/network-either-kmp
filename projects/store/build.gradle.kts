plugins {
    KotlinMultiplatform
    NexusPublish
    JaCoCo
    Dokka
    Versioning
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        commonMain {
            dependencies {
                projects.projects.apply {
                    api(resource)
                }

                libs.common.main.apply {
                    implementation(coroutines.core)
                }
            }
        }

        commonTest {
            dependencies {
                libs.common.test.apply {
                    implementation(coroutines.test)
                    implementation(kotest.assertions)
                    implementation(kotlin.test.annotations)
                    implementation(kotlin.test.common)
                    implementation(kotlin.test.junit)
                    implementation(turbine)
                }
            }
        }

        named("jvmTest")

        defaultLanguageSettings
    }
}
