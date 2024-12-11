plugins {
    java
}

subprojects {
    apply(plugin = "java")

    fun prop(name: String) = project.rootProject.property(name) as String

    group = prop("group")
    version = prop("version")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(project(":api"))
        compileOnly("io.netty:netty-all:4.1.97.Final")
        compileOnly("org.slf4j:slf4j-api:1.7.30")
    }
}