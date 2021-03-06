package org.example.netty.client.time.v1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.netty.util.ConfigUtil;

import java.net.InetSocketAddress;

/**
 * @author leganck
 * @date 2021/4/26 9:58
 **/
public class TimeClient {

    private final String host;
    private final Integer port;

    public TimeClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        String host = ConfigUtil.getServerHost();
        int port = ConfigUtil.getServerPort();
        new TimeClient(host, port).run();
    }

    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new TimeClientHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
