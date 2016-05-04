package com.izhengyin.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SocketServerHandler extends ChannelInboundHandlerAdapter {
	
	private int counter = 0;
	
	private byte[] bytes = null;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(ctx.channel().id());
		String body = (String)msg;
		System.out.println("received client message:"+body+" : count "+ ++counter);
		bytes = body.getBytes();
		ByteBuf resp = Unpooled.copiedBuffer(bytes);
		ctx.write(resp);
	}
}
