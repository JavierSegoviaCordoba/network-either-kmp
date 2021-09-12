plugins {
    `javiersc-kotlin-multiplatform-no-android`
    `kotlin-serialization`
    `javiersc-publish-kotlin-multiplatform`
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
                    implementation(jetbrains.kotlin.kotlinTestCommon)
                    implementation(jetbrains.kotlin.kotlinTestJunit)
                    implementation(kotest.kotestAssertionsCore)
                }
            }
        }

        named("jvmTest")
    }
}
