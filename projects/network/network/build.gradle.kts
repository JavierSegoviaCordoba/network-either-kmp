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

                libs.common.main.apply {
                    api(coroutines.core)
                    api(ktor.client.core)
                    api(serialization.json)
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
                    implementation(ktor.client.mock)
                    implementation(ktor.client.serialization)
                }
            }
        }

        named("jvmMain") {
            dependencies {
                libs.jvm.main.apply {
                    api(ktor.client.cio)
                    api(okHttp3.okhttp)
                    api(retrofit2.retrofit)
                }
            }
        }

        named("jvmTest") {
            dependencies {
                libs.jvm.test.apply {
                    implementation(okHttp3.mockWebServer)
                    implementation(retrofit2.converter.serialization)
                }
            }
        }

        defaultLanguageSettings
    }
}
