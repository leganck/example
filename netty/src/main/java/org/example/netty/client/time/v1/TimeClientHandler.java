package org.example.netty.client.time.v1;

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
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            String logMsg = new Date(currentTimeMillis).toString();
            log.info(logMsg);
            ctx.close();
        } finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
