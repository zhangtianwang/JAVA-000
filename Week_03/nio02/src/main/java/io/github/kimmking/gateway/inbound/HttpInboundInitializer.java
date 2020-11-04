package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.FilterHandler;
import io.github.kimmking.gateway.router.RouterHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.ArrayList;
import java.util.List;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
	
	private String proxyServer;
	
	public HttpInboundInitializer(String proxyServer) {
		this.proxyServer = proxyServer;
	}
	
	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
//		if (sslCtx != null) {
//			p.addLast(sslCtx.newHandler(ch.alloc()));
//		}
		p.addLast(new HttpServerCodec());
		//p.addLast(new HttpServerExpectContinueHandler());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		p.addLast(new RouterHandler(getRoutes()));
		p.addLast(new FilterHandler());
		p.addLast(new HttpInboundHandler(this.proxyServer));
	}

	private List<String> getRoutes(){
		List<String> routes = new ArrayList<String>();
		routes.add("localhost:8801");
		routes.add("localhost:8802");
		routes.add("localhost:8803");
		return routes;
	}
}
