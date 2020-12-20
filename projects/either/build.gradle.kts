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
        commonMain

        commonTest {
            dependencies {
                libs.common.test.apply {
                    implementation(kotest.assertions)
                    implementation(kotlin.test.common)
                    implementation(kotlin.test.annotations)
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
