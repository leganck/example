package org.example.netty.client.time.v4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.netty.util.LogUtil;

import java.util.logging.Logger;

/**
 * @author leganck
 * @date 2021/4/26 10:01
 **/
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LogUtil.getLogger();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        UnixTime m = (UnixTime) msg;
        log.info(m.toString());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
