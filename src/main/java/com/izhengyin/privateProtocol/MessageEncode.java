package com.izhengyin.privateProtocol;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class MessageEncode extends MessageToMessageEncoder<MessageEntity>{
	public MessageEncode() {
		System.out.println("MessageEncode");
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageEntity msg, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(msg.toString());
	}
	
}
