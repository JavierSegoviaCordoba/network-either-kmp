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
                    implementation(either)
                }

                libs.common.main.apply {
                    implementation(coroutines.core)
                    implementation(ktor.client.core)
                    implementation(serialization.json)
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
                    implementation(okHttp3.okhttp)
                    implementation(retrofit2.retrofit)
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
