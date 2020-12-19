
rootProject.name = "either"

enableFeaturePreview("GRADLE_METADATA")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        jcenter()
    }

    versionCatalogs {
        create("commonTestLibs") { from(files("gradle/common-test-dependencies.toml")) }
        create("jvmTestLibs") { from(files("gradle/jvm-test-dependencies.toml")) }
    }
}

include(":projects:either")
