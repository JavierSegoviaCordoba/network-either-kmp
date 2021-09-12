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

        named("jvmMain") {
            dependencies {
                libs.apply {
                    api(squareup.okhttp3.okhttp)
                    api(squareup.retrofit2.retrofit)
                }
            }
        }

        named("jvmTest") {
            dependencies {
                libs.apply {
                    implementation(jakewharton.retrofit.retrofit2KotlinxSerializationConverter)
                    implementation(squareup.okhttp3.mockwebserver)
                }
            }
        }
    }
}
