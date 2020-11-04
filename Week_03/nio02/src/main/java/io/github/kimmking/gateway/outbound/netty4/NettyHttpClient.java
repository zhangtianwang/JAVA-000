package io.github.kimmking.gateway.outbound.netty4;

import io.github.kimmking.gateway.listener.NettyHttpListener;
import io.github.kimmking.gateway.outbound.netty4.NettyHttpClientOutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;


public class NettyHttpClient {


    public void connect(SocketAddress address, ChannelHandlerContext context, Object msg) {
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(address)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());
                            socketChannel.pipeline().addLast(new NettyHttpClientOutboundHandler());
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync().addListener(new NettyHttpListener(context, msg));
        } catch (Exception ex) {

        }
    }

}

//package io.github.kimmking.gateway.outbound;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.HttpRequestEncoder;
//import io.netty.handler.codec.http.HttpResponseDecoder;
//
//public class NettyHttpClient {
//    public void connect(String host, int port) throws Exception {
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(workerGroup);
//            b.channel(NioSocketChannel.class);
//            b.option(ChannelOption.SO_KEEPALIVE, true);
//            b.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
//                    ch.pipeline().addLast(new HttpResponseDecoder());
//                     客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
//                    ch.pipeline().addLast(new HttpRequestEncoder());
//                    ch.pipeline().addLast(new HttpClientOutboundHandler());
//                }
//            });
//
//            // Start the client.
//            ChannelFuture f = b.connect(host, port).sync();
//
//           
//            f.channel().write(request);
//            f.channel().flush();
//            f.channel().closeFuture().sync();
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        NettyHttpClient client = new NettyHttpClient();
//        client.connect("127.0.0.1", 8844);
//    }
//}