pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":api")
include(":fabric")
include(":paper")
include(":spigot")
include(":http")
include(":examples")
include(":examples:injectors")
include(":examples:injectors:jetty")
include(":examples:javalin")
include(":examples:spring")
