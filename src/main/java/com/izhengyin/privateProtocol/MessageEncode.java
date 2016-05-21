package com.izhengyin.privateProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncode extends MessageToByteEncoder<MessageEntity>{

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageEntity msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		byte[] bytes = msg.toString().getBytes();
		out.writeBytes(bytes);
	}
	
}
