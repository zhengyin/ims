package com.izhengyin.privateProtocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.setC("H");
		messageEntity.setB("Hello server!");
		ctx.writeAndFlush(messageEntity);
		System.out.println("channelActive writeAndFlush!");
	}
}
