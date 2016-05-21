package com.izhengyin.privateProtocol;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncode extends MessageToByteEncoder<MessageEntity>{

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageEntity msg, ByteBuf out) throws Exception {
		byte[] bytes = msg.toString().getBytes("UTF-8");
		out.writeInt(bytes.length);
		out.writeBytes(bytes);
	}
}
