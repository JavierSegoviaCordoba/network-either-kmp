plugins {
    `kotlin-multiplatform`
    `javiersc-kotlin-library`
    `javiersc-publish`
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        commonMain {
            dependencies {
                projects.projects.apply {
                    api(eitherCore)
                    api(network.networkCore)
                    api(resourceCore)
                }

                libs.apply {
                    implementation(jetbrains.kotlinx.kotlinxCoroutinesCore)
                    api(jetbrains.kotlinx.kotlinxSerializationJson)
                    api(ktor.ktorClientCore)
                }
            }
        }

        commonTest {
            dependencies {
                libs.apply {
                    implementation(jetbrains.kotlin.kotlinTest)
                    implementation(kotest.kotestAssertionsCore)
                    implementation(ktor.ktorClientMock)
                    implementation(ktor.ktorClientSerialization)
                }
            }
        }
    }
}
