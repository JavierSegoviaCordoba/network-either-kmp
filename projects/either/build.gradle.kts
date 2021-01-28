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
        commonMain

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
