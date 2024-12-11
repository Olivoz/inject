package net.mcbrawls.inject.example.javalin;

import io.github.olivoz.springinjectspigot.spring.JettyInjector;
import io.javalin.Javalin;
import net.mcbrawls.inject.paper.InjectPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.LocalConnector;

public class JavalinInjectExamplePlugin extends JavaPlugin {

    private Javalin app = null;

    @Override
    public void onEnable() {
        app = Javalin.create(javalinConfig -> {
                    javalinConfig.useVirtualThreads = true;
                    javalinConfig.showJavalinBanner = false;

                    javalinConfig.jetty.addConnector((server, httpConfig) -> {
                        LocalConnector connector = new LocalConnector(server, new HttpConnectionFactory(httpConfig));
                        InjectPaper.INSTANCE.registerInjector(new JettyInjector(connector));

                        return connector;
                    });
                })
                .get("/hello", ctx -> ctx.result("Hello World! \n- Javalin"));

        app.start();
    }

    @Override
    public void onDisable() {
        if (app != null)
            app.stop();
    }
}
