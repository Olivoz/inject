# inject
Inject is a simple server-side library to allow developers to inject into Netty easier.

## Example
This uses the `HttpInjector` class to respond to HTTP requests to the Minecraft
server.

```java
class MyEpicHttpInjector extends HttpInjector {
    @Override
    public HttpByteBuf intercept(ChannelHandlerContext ctx, HttpRequest request) {
        HttpByteBuf buf = HttpByteBuf.httpBuf(ctx);
        buf.writeStatusLine("1.1", 200, "OK");
        buf.writeText("Hello, from Minecraft!");
        return buf;
    }
}
```

## Registration
For Fabric, use the `InjectFabric` class:
```java
public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        InjectFabric.INSTANCE.registerInjector(new MyEpicHttpInjector());
    }
}
```

For Paper, use the `InjectPaper` class:
```java
public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        InjectPaper.INSTANCE.registerInjector(new MyEpicHttpInjector());
    }
}
```

For Spigot, use the `InjectSpigot` class:
```java
public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        InjectSpigot.INSTANCE.registerInjector(new MyEpicHttpInjector());
    }
}
```

> [!CAUTION]
> The Spigot module does not function on Paper. Please use the Paper module and check what the server
> is running on, and decide based on that. The Spigot module uses reflection into internals which are
> renamed on Paper due to mojang mappings at runtime.

This will register an HTTP injector which will respond with `Hello, from Minecraft!`
to any HTTP request to the Minecraft port.

```bash
$ curl http://localhost:25565
Hello, from Minecraft!
```

## Usage
Add the andante repo to gradle:
```kt
repositories {
    maven("https://maven.andante.dev/releases/")
}
```

Add the dependency:
```kt
dependencies {
    implementation("net.mcbrawls.inject:api:VERSION")
    
    // HTTP-related things:
    implementation("net.mcbrawls.inject:http:VERSION")

    // Fabric:
    include(modImplementation("net.mcbrawls.inject:fabric:VERSION")!!)
 
    // Paper:
    implementation("net.mcbrawls.inject:paper:VERSION")

    // Spigot:
    implementation("net.mcbrawls.inject:spigot:VERSION")
}
```

Replace `VERSION` with the latest version from the releases tab.
