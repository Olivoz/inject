package io.github.olivoz.springinjectspigot.spring;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.mcbrawls.inject.api.Injector;
import net.mcbrawls.inject.api.InjectorContext;
import net.mcbrawls.inject.api.PacketDirection;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.LocalConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public final class JettyInjector extends Injector {

    private final Logger logger = LoggerFactory.getLogger("JavalinInjector " + this.hashCode());
    private final LocalConnector connector;

    public JettyInjector(LocalConnector connector) {
        this.connector = connector;
    }

    @Override
    public boolean isRelevant(InjectorContext ctx, PacketDirection direction) {
        return HttpMethod.lookAheadGet(ctx.message().nioBuffer()) != null;
    }

    @Override
    public boolean onRead(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        ByteBuffer request = buf.nioBuffer();
        ByteBuffer response = connector.getResponse(request);

        ctx.writeAndFlush(Unpooled.wrappedBuffer(response))
                .addListener(ChannelFutureListener.CLOSE)
                .addListener((future) -> {
                    Throwable cause = future.cause();
                    if (cause != null) {
                        this.logger.error("Write failed", cause);
                    }
                });

        return true;
    }
}
