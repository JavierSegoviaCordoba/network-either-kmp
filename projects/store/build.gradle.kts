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

        named("jvmTest") {
            dependencies {
                libs.jvm.test.apply {
                    implementation(kotlin.test.junit)
                }
            }
        }

        defaultLanguageSettings
    }
}
