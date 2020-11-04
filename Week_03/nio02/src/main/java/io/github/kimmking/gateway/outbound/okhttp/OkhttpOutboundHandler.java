package io.github.kimmking.gateway.outbound.okhttp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.internal.StringUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {

    private String serverUrl;


    public OkhttpOutboundHandler(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;
        try {
            String uri = fullRequest.uri();
            String value = httpGet(this.serverUrl + uri);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
            String userName = fullRequest.headers().get("nio");
            if (!StringUtil.isNullOrEmpty(userName)) {
                response.headers().set("nio", userName);
            }

        } catch (Exception e) {
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }

    public static String httpGet(String url) throws IOException {
        OkHttpClient httpClient = new okhttp3.OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string(); // 返回的是string 类型，json的mapper可以直接处理
    }

}
