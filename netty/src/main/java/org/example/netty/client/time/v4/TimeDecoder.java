package org.example.netty.client.time.v4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author leganck
 * @date 2021/4/26 10:44
 **/
public class TimeDecoder extends ByteToMessageDecoder {
    public static final int DATA_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < DATA_LENGTH) {
            return;
        }
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
