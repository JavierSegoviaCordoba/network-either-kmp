
rootProject.name = "either"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        val massiveCatalogs: String by settings

        create("libs") { from("com.javiersc.massive-catalogs:libs-catalog:$massiveCatalogs") }
        create("pluginLibs") {
            from("com.javiersc.massive-catalogs:plugins-catalog:$massiveCatalogs")
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
