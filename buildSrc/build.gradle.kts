plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    // TODO: remove when the next issue is fixed:
    //  https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(files(pluginLibs.javaClass.superclass.protectionDomain.codeSource.location))

    pluginLibs.apply {
        implementation(javiersc.gradlePlugins.allProjects)
        implementation(javiersc.gradlePlugins.changelog)
        implementation(javiersc.gradlePlugins.codeAnalysis)
        implementation(javiersc.gradlePlugins.codeFormatter)
        implementation(javiersc.gradlePlugins.docs)
        implementation(javiersc.gradlePlugins.kotlinMultiplatform)
        implementation(javiersc.gradlePlugins.nexus)
        implementation(javiersc.gradlePlugins.pluginAccessors)
        implementation(javiersc.gradlePlugins.publishKotlinMultiplatform)
        implementation(javiersc.gradlePlugins.readmeBadges)
        implementation(javiersc.gradlePlugins.versioning)

        implementation(android.toolsBuild.gradle)
        implementation(jetbrains.kotlin.kotlinGradlePluginX)
        implementation(jetbrains.kotlin.kotlinSerialization)
        implementation(jetbrains.kotlinx.binaryCompatibilityValidator)
    }
}
