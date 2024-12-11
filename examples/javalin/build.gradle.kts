import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    id("io.papermc.paperweight.userdev") // Version controlled by main build.gradle.kts
    id("xyz.jpenilla.run-paper") // Version controlled by main build.gradle.kts - Adds runServer and runMojangMappedServer tasks for testing
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0" // Generates plugin.yml based on the Gradle config
    id("com.gradleup.shadow") version "8.3.4"
}

//name = "JavalinInjectExamplePlugin"
description = "Run a javalin server on the server port by injecting Netty."

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
    implementation(project(":paper"))
    implementation(project(":spigot"))

    compileOnly("io.javalin:javalin:6.3.0") // Loaded via library loader
    implementation(project(":examples:injectors:jetty")) {
        // No need to fetch transitive dependencies. Using provided dependencies by Javalin
        isTransitive = false
    }
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    // You can relocate inject here to prevent conflicts
    /*
    shadowJar {
        relocate("net.mcbrawls.inject", "${project.group}.${project.name.lowercase(Locale.ROOT)}.shaded.inject")
    }
     */
}

bukkitPluginYaml {
    main = "net.mcbrawls.inject.example.javalin.JavalinInjectExamplePlugin"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.add("Olivo (Olivoz)")
    apiVersion = "1.21.1"
    libraries.addAll(
        "io.javalin:javalin:6.3.0"
    )
}