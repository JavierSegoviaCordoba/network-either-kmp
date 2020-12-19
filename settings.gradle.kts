
rootProject.name = "either"

enableFeaturePreview("GRADLE_METADATA")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        jcenter()
    }

    versionCatalogs {
        create("gradlePluginLibs") { from(files("gradle/gradle-plugin-dependencies.toml")) }
    }
}

include(":either")
