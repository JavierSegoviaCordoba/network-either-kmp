rootProject.name = providers.gradleProperty("allProjects.name").forUseAtConfigurationTime().get()

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    dependencyResolutionManagement {
        versionCatalogs {
            create("libs") { from(files("gradle/libs.toml")) }
            create("pluginLibs") { from(files("gradle/pluginLibs.toml")) }
        }
    }
}

include(
    ":projects:either-core",
)

include(
    ":projects:network:network-core",
    ":projects:network:network-logger",
    ":projects:network:network-resource",
)

include(
    ":projects:resource-core",
)
