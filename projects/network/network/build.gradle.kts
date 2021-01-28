plugins {
    KotlinMultiplatform
    NexusPublish
    JaCoCo
    Dokka
    EitherVersioning
}

val dokkaJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    dependsOn(tasks.dokkaJavadoc)
}

kotlin {
    explicitApi()

    jvm {
        mavenPublication {
            artifact(dokkaJar)
        }
    }

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
                    implementation(kotlin.test.common)
                    implementation(kotlin.test.annotations)
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
                    implementation(kotlin.test.junit)
                    implementation(okHttp3.mockWebServer)
                    implementation(retrofit2.converter.serialization)
                }
            }
        }

        defaultLanguageSettings
    }
}
