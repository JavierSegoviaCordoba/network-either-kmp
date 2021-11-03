plugins {
    `kotlin-multiplatform`
    `javiersc-kotlin-library`
    `javiersc-publish`
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        commonMain

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
