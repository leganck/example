package org.example.netty.other.heartbeat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


/**
 * @author leganck
 * @date 2021/4/27 13:38
 **/
public class HeartbeatHandlerInitializer extends ChannelInitializer<Channel> {
    /**
     * 读超时
     */
    private static final int READ_IDEL_TIME_OUT = 4;
    /**
     * 写超时
     */
    private static final int WRITE_IDEL_TIME_OUT = 5;
    /**
     * 所有超时
     */
    private static final int ALL_IDEL_TIME_OUT = 7;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS))
                .addLast(new HeartbeatServerHandler());
    }
}
