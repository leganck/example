package org.example.netty.server.simplechat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.util.logging.Logger;

/**
 * @author leganck
 * @date 2021/4/26 15:20
 **/
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
    public static final String SIMPLE_CHAT_CLIENT = "SimpleChatClient";
    private static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final Logger log = Logger.getLogger(this.getClass().getName());


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        CHANNELS.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        CHANNELS.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        CHANNELS.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        Channel incoming = ctx.channel();
        for (Channel channel : CHANNELS) {
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
            } else {
                channel.writeAndFlush("[you]" + msg + "\n");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        log.info(() -> SIMPLE_CHAT_CLIENT + ":" + remoteAddress + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        log.info(() -> SIMPLE_CHAT_CLIENT + ":" + remoteAddress + "掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        log.info(() -> SIMPLE_CHAT_CLIENT + ":" + remoteAddress + "异常");
        cause.printStackTrace();
        ctx.close();
    }
}
