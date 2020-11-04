package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

public class FilterHandler  extends ChannelInboundHandlerAdapter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        HttpHeaders headers = fullRequest.headers();
        String uri = fullRequest.uri();
        if(uri.contains("nio")){
            headers.set("nio","zhangtianwang");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest)msg;
        filter(request,ctx);
        super.channelRead(ctx, msg);
    }
}
