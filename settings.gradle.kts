
rootProject.name = "either"

enableFeaturePreview("GRADLE_METADATA")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        jcenter()
    }
}

include(":projects:either")
include(":projects:network:network")
include(":projects:network:network-logger")
include(":projects:network:network-resource")
include(":projects:resource")
include(":projects:store")
