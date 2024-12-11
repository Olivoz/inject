package net.mcbrawls.inject.examples.paper;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.Exclusion;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class InjectDependencyLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(
                new RemoteRepository.Builder(
                        "central",
                        "default",
                        "https://repo1.maven.org/maven2/"
                ).build()
        );

        resolver.addDependency(
                new Dependency(
                        new DefaultArtifact("org.springframework.boot", "spring-boot-starter-web", "jar", "3.3.5"),
                        JavaScopes.COMPILE,
                        false,
                        excludes(
                                "org.springframework.boot:spring-boot-starter-tomcat",
                                "org.springframework.boot:spring-boot-starter-logging",

                                // Already provided by Spigot
                                "org.yaml:snakeyaml",
                                "org.ow2.asm:asm",
                                "org.ow2.asm:asm-commons",
                                "org.ow2.asm:asm-tree",
                                "org.slf4j:slf4j-api"
                        )
                )
        );

        resolver.addDependency(
                new Dependency(
                        new DefaultArtifact(
                                "org.springframework.boot:spring-boot-starter-jetty:3.3.5"
                        ),
                        JavaScopes.COMPILE
                )
        );

        classpathBuilder.addLibrary(resolver);
    }

    private static Set<Exclusion> excludes(String... dependencies) {
        Set<Exclusion> exclusions = HashSet.newHashSet(dependencies.length);

        for (String dependency : dependencies) {
            String[] split = dependency.split(":");
            exclusions.add(new Exclusion(
                    split[0],
                    split[1],
                    "*",
                    "*"
            ));
        }

        return exclusions;
    }
}