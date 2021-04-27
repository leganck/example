package org.example.netty.server.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.netty.client.echo.EchoClient;
import org.example.netty.util.ConfigUtil;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * 客户端 {@link EchoClient}
 *
 * @author leganck
 * @date 2021/4/23 15:49
 **/
public class EchoServer {
    private static final Logger log = Logger.getLogger(EchoServer.class.getName());
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port = ConfigUtil.getServerPort();
        new EchoServer(port).start();
    }

    public void start() {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(nioEventLoopGroup, nioEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            ChannelFuture future = server.bind().sync();
            log.info(() -> EchoServer.class.getName() + " started and listen on " + future.channel().localAddress());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }

    }
}
