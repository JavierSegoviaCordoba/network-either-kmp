plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    jcenter()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    gradlePluginLibs.apply {
        implementation(dependencyUpdates)
        implementation(detekt)
        implementation(dokka)
        implementation(kotlin.gradlePlugin)
        implementation(nexus.staging)
        implementation(nexus.publish)
    }
}
