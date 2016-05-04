package com.izhengyin.privateProtocol;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessageDecode extends MessageToMessageDecoder<MessageEntity>{

	@Override
	protected void decode(ChannelHandlerContext ctx, MessageEntity messageEntity, List<Object> arg2) throws Exception {
			
	}
		
}
