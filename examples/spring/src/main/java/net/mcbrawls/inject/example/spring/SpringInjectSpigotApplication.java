package net.mcbrawls.inject.example.spring;

import io.github.olivoz.springinjectspigot.spring.JettyInjector;
import org.bukkit.Bukkit;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.LocalConnector;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringInjectSpigotApplication {

    // Tell Spring to use Jetty even though Tomcat is present!
    @Bean
    JettyServletWebServerFactory servletWebServerFactory(ObjectProvider<JettyServerCustomizer> serverCustomizers) {
        JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
        
        // For logging reasons only. Does not have any actual effect during runtime
        factory.setPort(Bukkit.getPort());

        // We can just add our customizer here instead of registering it externally
        factory.addServerCustomizers(server -> {
            LocalConnector connector = new LocalConnector(server);

            // Replace builtin connectors with the local one
            server.setConnectors(new Connector[]{connector});

            // Register the injector
            SpringInjectExamplePlugin.getInjectForPlatform().registerInjector(new JettyInjector(connector));
        });

        factory.getServerCustomizers().addAll(serverCustomizers.orderedStream().toList());
        return factory;
    }
}
