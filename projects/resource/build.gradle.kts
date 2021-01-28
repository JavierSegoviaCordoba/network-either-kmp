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
                    api(either)
                }
            }
        }

        commonTest {
            dependencies {
                libs.common.test.apply {
                    implementation(kotest.assertions)
                    implementation(kotlin.test.annotations)
                    implementation(kotlin.test.common)
                    implementation(kotlin.test.junit)
                }
            }
        }

        named("jvmTest")

        defaultLanguageSettings
    }
}
