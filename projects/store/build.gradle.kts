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
                projects.projects.apply { api(resourceCore) }

                libs.apply {
                    implementation(jetbrains.kotlinx.kotlinxCoroutinesCore)
                    implementation(javiersc.runBlocking.runBlockingCore)
                }
            }
        }

        commonTest {
            dependencies {
                libs.apply {
                    implementation(cash.turbine.turbine)
                    implementation(javiersc.runBlocking.suspendTest)
                    implementation(jetbrains.kotlinx.kotlinxCoroutinesTest)
                    implementation(jetbrains.kotlin.kotlinTest)
                    implementation(kotest.kotestAssertionsCore)
                }
            }
        }

        named("jvmTest")
    }
}
