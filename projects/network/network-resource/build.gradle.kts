plugins {
    `javiersc-kotlin-multiplatform-no-android`
    `javiersc-publish-kotlin-multiplatform`
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
