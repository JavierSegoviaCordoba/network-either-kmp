plugins {
    `kotlin-multiplatform`
    `kotlin-serialization`
    `javiersc-kotlin-config`
    `javiersc-publish`
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        commonMain {
            dependencies {
                projects.projects.apply {
                    api(network.networkCore)
                }

                libs.apply {
                    api(javiersc.mokoki.mokokiSerialization)
                }
            }
        }

        commonTest {
            dependencies {
                libs.apply {
                    implementation(jetbrains.kotlin.kotlinTest)
                    implementation(kotest.kotestAssertionsCore)
                }
            }
        }
    }
}
