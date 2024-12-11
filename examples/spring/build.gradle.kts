import xyz.jpenilla.resourcefactory.paper.paperPluginYaml

plugins {
    id("io.papermc.paperweight.userdev") // Version controlled by main build.gradle.kts
    id("xyz.jpenilla.run-paper") // Version controlled by main build.gradle.kts - Adds runServer and runMojangMappedServer tasks for testing
    id("xyz.jpenilla.resource-factory") version "1.2.0" // Generates (paper-)plugin.yml based on the Gradle config
    id("com.gradleup.shadow") version "8.3.4"
}

description = "Run a javalin server on the server port by injecting Netty."

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
    implementation(project(":paper"))
    implementation(project(":spigot"))
    implementation(project(":examples:injectors:jetty")) {
        // No need to fetch transitive dependencies. Using provided dependencies by Spring Jetty Starter
        isTransitive = false
    }

    compileOnly("org.springframework.boot:spring-boot-starter-web:3.3.5") {
        modules {
            exclude("org.springframework.boot", "spring-boot-starter-logging")
            module("org.springframework.boot:spring-boot-starter-tomcat") {
                replacedBy("org.springframework.boot:spring-boot-starter-jetty", "Use Jetty instead of Tomcat")
            }
        }
    }

    compileOnly("org.springframework.boot:spring-boot-starter-jetty:3.3.5")
}

sourceSets.main {
    resourceFactory {
        bukkitPluginYaml {
            main = "net.mcbrawls.inject.example.spring.SpringInjectExamplePlugin"
            authors.add("Olivo (Olivoz)")
            apiVersion = "1.21.1"
            libraries.addAll(
                "org.springframework.boot:spring-boot-starter-web:3.3.5",
                "org.springframework.boot:spring-boot-starter-jetty:3.3.5"
            )
        }

        paperPluginYaml {
            main = "net.mcbrawls.inject.example.spring.SpringInjectExamplePlugin"
            loader = "net.mcbrawls.inject.example.spring.SpringInjectExampleLoader"
            authors.add("Olivo (Olivoz)")
            apiVersion = "1.21.1"
        }
    }
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    // If you wish to shade you can relocate and exclude dependencies here to prevent conflicts.
    /*
    shadowJar {
        relocate("net.mcbrawls.inject", "${project.group}.${project.name.lowercase(Locale.ROOT)}.shaded.inject")
    }
     */
}