package org.example.netty.client.time.v2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.logging.Logger;

/**
 * @author leganck
 * @date 2021/4/26 10:01
 **/
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private static final int DATA_LENGTH = 4;
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private ByteBuf byteBuf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        byteBuf = ctx.alloc().buffer(DATA_LENGTH);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        byteBuf.release();
        byteBuf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        byteBuf.writeBytes(m);
        m.release();
        if (byteBuf.readableBytes() >= DATA_LENGTH) {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            String logMsg = new Date(currentTimeMillis).toString();
            log.info(logMsg);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
