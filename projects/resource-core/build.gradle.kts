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
            }
        }

        commonTest {
            dependencies {
                libs.apply {
                    implementation(jetbrains.kotlin.kotlinTestMultiplatform)
                    implementation(kotest.kotestAssertionsCore)
                }
            }
        }

        named("jvmTest")
    }
}
