plugins {
    `javiersc-kotlin-multiplatform`
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
                }

                libs.apply {
                    implementation(javiersc.runBlocking.runBlocking)
                    api(jetbrains.kotlinx.kotlinxCoroutinesCore)
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
                    api(ktor.ktorClientCio)
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
