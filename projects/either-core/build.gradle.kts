plugins {
    `javiersc-kotlin-multiplatform`
    `javiersc-publish-kotlin-multiplatform`
}

kotlin {
    explicitApi()

    jvm()

    sourceSets {
        commonMain

        commonTest {
            dependencies {
                libs.apply {
                    implementation(kotest.kotestAssertionsCore)
                    implementation(jetbrains.kotlin.kotlinTest)
                }
            }
        }

        named("jvmTest")
    }
}
