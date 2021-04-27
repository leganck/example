package org.example.netty.other.simplechat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * @author leganck
 * @date 2021/4/26 15:38
 **/
public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel> {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        InetSocketAddress remoteAddress = ch.remoteAddress();
        ch.pipeline().addLast("frame", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
                .addLast("handler", new SimpleChatServerHandler());
        log.info(() -> "SimpleChatClient:" + remoteAddress + "连接上线");
    }
}
