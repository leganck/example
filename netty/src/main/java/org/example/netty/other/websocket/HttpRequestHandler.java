package org.example.netty.other.websocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import org.example.netty.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * @author leganck
 * @date 2021/4/27 10:09
 **/
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final File INDEX;
    private final Logger log = LogUtil.getLogger();
    private final String wsUrl;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "WebSocketChatClient.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate WebsocketChatClient.html", e);
        }
    }


    public HttpRequestHandler(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(fullHttpResponse);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 忽略并传递给下一个Handler
        if (wsUrl.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            // 处理http/1.1 100 Continue请求
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            try (RandomAccessFile file = new RandomAccessFile(INDEX, "r")) {
                HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
                response.headers().set(HttpHeaderNames.CONNECTION, "text/html; charset=UTF-8");
                boolean keepAlive = HttpUtil.isKeepAlive(request);
                if (keepAlive) {
                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                }
                ctx.write(response);
                if (ctx.pipeline().get(SslHandler.class) == null) {
                    ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
                } else {
                    ctx.write(new ChunkedNioFile(file.getChannel()));
                }
                //标记响应完成
                ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                if (!keepAlive) {
                    future.addListener(ChannelFutureListener.CLOSE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        log.info(() -> "Client:" + incoming.remoteAddress() + "异常");
        cause.printStackTrace();
        ctx.close();
    }
}
