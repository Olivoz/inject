package net.mcbrawls.inject.example.spring;

import net.mcbrawls.inject.api.InjectPlatform;
import net.mcbrawls.inject.paper.InjectPaper;
import net.mcbrawls.inject.spigot.InjectSpigot;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class SpringInjectExamplePlugin extends JavaPlugin {

    private ConfigurableApplicationContext applicationContext;

    public static InjectPlatform getInjectForPlatform() {
        ClassLoader classLoader = SpringInjectExamplePlugin.class.getClassLoader();
        if (ClassUtils.isPresent("io.papermc.paper.network.ChannelInitializeListenerHolder", classLoader))
            return InjectPaper.INSTANCE;

        return InjectSpigot.INSTANCE;
    }

    @Override
    public void onEnable() {
        // Log4J is already setup. Spring does not need to do so
        System.setProperty(LoggingSystem.SYSTEM_PROPERTY, LoggingSystem.NONE);

        // Load application.properties
        Properties props = new Properties();
        try {
            props.load(getResource("application.properties"));
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Failed to locate 'application.properties'! Shutting down plugin...", ex);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Spring does not start properly without this thread hackery...
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            ClassLoader classLoader = this.getClass().getClassLoader();
            applicationContext = executor.submit(() -> {
                Thread.currentThread().setContextClassLoader(classLoader);

                return new SpringApplicationBuilder(SpringInjectSpigotApplication.class)
                        .resourceLoader(new DefaultResourceLoader(classLoader))
                        .bannerMode(Banner.Mode.OFF)
                        .logStartupInfo(false)
                        .properties(props)
                        .run();
            }).get();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "Spring has shutdown unexpectedly! Shutting down plugin...", ex);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (applicationContext != null) {
            applicationContext.close();
            applicationContext = null;
        }
    }
}
