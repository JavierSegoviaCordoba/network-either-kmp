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
        implementation(dokka.core)
        implementation(dokka.gradlePlugin)
        implementation(kotlin.gradlePlugin)
        implementation(nexus.staging)
        implementation(nexus.publish)
    }
}
