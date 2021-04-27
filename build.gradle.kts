import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `javiersc-versioning`
    `javiersc-changelog`
    `javiersc-code-analysis`
    `javiersc-dependency-updates`
    `javiersc-docs`
    `kotlinx-binary-compatibility-validator`
    `javiersc-nexus`
    `javiersc-readme-badges-generator`
}

tasks {
    withType<Test> {
        maxParallelForks = Runtime.getRuntime().availableProcessors()
        useJUnitPlatform()
        useTestNG()
    }
}

allprojects.forEach { project ->
    project.tasks.withType<KotlinCompile>().all {
        kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
    }
}
