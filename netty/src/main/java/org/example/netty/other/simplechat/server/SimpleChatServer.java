package org.example.netty.other.simplechat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.netty.other.simplechat.client.SimpleChatClient;
import org.example.netty.util.LogUtil;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * 客户端 {@link SimpleChatClient}
 *
 * @author leganck
 * @date 2021/4/26 16:06
 **/
public class SimpleChatServer {
    private final Logger log = LogUtil.getLogger();

    private final int port;

    public SimpleChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new SimpleChatServer(port).run();
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SimpleChatServerInitializer())
                .localAddress(new InetSocketAddress(port))
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        log.info("SimpleChatServer 启动");
        try {
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

