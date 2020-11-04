package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.FilterHandler;
import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.github.kimmking.gateway.outbound.netty4.NettyHttpClient;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;
    private HttpOutboundHandler handler;
    private OkhttpOutboundHandler okHandler;

    private static NettyHttpClient client = new NettyHttpClient();

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        handler = new HttpOutboundHandler(this.proxyServer);
        okHandler= new OkhttpOutboundHandler(this.proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            String serverUrl = fullRequest.headers().get("server");
            String[] arr = serverUrl.split(":");
            SocketAddress address = new InetSocketAddress(arr[0],Integer.parseInt(arr[1]));
            client.connect(address,ctx,msg);

            handler.handle(fullRequest, ctx);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }



//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }

}
