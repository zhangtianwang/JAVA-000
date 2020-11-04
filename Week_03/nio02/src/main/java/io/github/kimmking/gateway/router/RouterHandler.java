package io.github.kimmking.gateway.router;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;
import java.util.Random;

public class RouterHandler extends ChannelInboundHandlerAdapter implements HttpEndpointRouter {

    private List<String> endpoints;

    public RouterHandler(List<String> endpoints){
        this.endpoints = endpoints;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest)msg;
        String url = route(this.endpoints);
        request.headers().add("server",url);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public String route(List<String> endpoints) {
        int idx = new Random().nextInt(endpoints.size());
        return endpoints.get(idx);
    }
}
