package com.izhengyin.protocol.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class WebSocketServerMessageHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		WebSocketMessage wsMessage  = (WebSocketMessage)msg;
		System.out.println(wsMessage);
	}
}
