package com.izhengyin.privateProtocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.setChannel("H");
		messageEntity.setBody("Hello server!");
		ctx.writeAndFlush(messageEntity);
		System.out.println("channelActive writeAndFlush!");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String string = (String)msg;
		System.out.println(string);
	}
}
