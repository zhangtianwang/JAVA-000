package io.github.kimmking.gateway.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class NettyHttpListener implements ChannelFutureListener {

    private ChannelHandlerContext context;
    private Object msg;

    public NettyHttpListener(ChannelHandlerContext context,Object msg){
        this.context = context;
        this.msg = msg;
    }


    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if(channelFuture.isSuccess()){
            channelFuture.channel().writeAndFlush(msg);
        }else{
            context.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_TIMEOUT));
            context.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
