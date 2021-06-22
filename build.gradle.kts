import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `javiersc-versioning`
    `javiersc-all-projects`
    `javiersc-changelog`
    `javiersc-code-analysis`
    `javiersc-code-formatter`
    `javiersc-dependency-updates`
    `javiersc-docs`
    `kotlinx-binary-compatibility-validator`
    `javiersc-nexus`
    `javiersc-readme-badges-generator`
}

allprojects.forEach { project ->
    project.tasks.withType<KotlinCompile>().all {
        kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
    }
}
