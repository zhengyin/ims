package com.izhengyin.privateProtocol;

import java.util.List;

import org.jboss.marshalling.MarshallerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessageBinaryDecode extends MessageToMessageDecoder<ByteBuf>{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(msg.getInt(msg.readerIndex()));
		
		byte[] bytes = new byte[msg.readableBytes()];
		
		msg.readBytes(bytes);
		
		System.out.println(new String(bytes,"UTF-8"));
		
//		System.out.println(msg.readableBytes());
		
	//	out.add(msg);
	}
	
}
