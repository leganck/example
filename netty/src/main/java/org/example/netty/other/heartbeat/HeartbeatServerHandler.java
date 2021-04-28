package org.example.netty.other.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.example.netty.util.LogUtil;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author leganck
 * @date 2021/4/27 13:53
 **/
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
    private static final ByteBuf HEARTBEAT_SEQUENCE =
            Unpooled.wrappedUnmodifiableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8));
    private final Logger log = LogUtil.getLogger();
    private final Map<IdleState, String> idleStateMap = new EnumMap<>(IdleState.class);

    public HeartbeatServerHandler() {
        idleStateMap.put(IdleState.READER_IDLE, "read idle");
        idleStateMap.put(IdleState.WRITER_IDLE, "write idle");
        idleStateMap.put(IdleState.ALL_IDLE, "all idle");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String type = idleStateMap.get(event.state());
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(
                    ChannelFutureListener.CLOSE_ON_FAILURE);
            log.info(() -> ctx.channel().remoteAddress() + "超时类型：" + type);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
