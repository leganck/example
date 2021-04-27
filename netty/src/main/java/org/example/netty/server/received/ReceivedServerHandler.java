package org.example.netty.server.received;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

/**
 * @author leganck
 * @date 2021/4/26 8:44
 **/
public class ReceivedServerHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        log.info(() -> in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
