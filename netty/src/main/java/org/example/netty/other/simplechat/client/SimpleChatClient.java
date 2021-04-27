package org.example.netty.other.simplechat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.netty.other.simplechat.server.SimpleChatServer;
import org.example.netty.util.ConfigUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * 服务端 {@link SimpleChatServer}
 *
 * @author leganck
 * @date 2021/4/26 16:22
 **/
public class SimpleChatClient {

    private final String host;
    private final int port;

    public SimpleChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        String host = ConfigUtil.getServerHost();
        int port = ConfigUtil.getServerPort();
        new SimpleChatClient(host, port).run();
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new SimpleChatClientInitializer());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            Channel channel = bootstrap.connect().sync().channel();
            while (true) {
                channel.writeAndFlush(in.readLine() + "\r\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
