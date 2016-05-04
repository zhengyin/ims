package com.izhengyin.privateProtocol;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessageDecode extends MessageToMessageDecoder<String>{

	@Override
	protected void decode(ChannelHandlerContext ctx, String s, List<Object> arg2) throws Exception {
			System.out.println(s);
	}
		
}
