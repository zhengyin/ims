package com.izhengyin.privateProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MessageDecode extends LengthFieldBasedFrameDecoder{
	
	
	public MessageDecode( int maxFrameLength,int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		
		
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		
		
	
		
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if(frame == null){
			return null;
		}
		byte[] bytes = new byte[frame.readableBytes()];
		frame.readBytes(bytes);
		String msg = new String(bytes,"UTF-8");
		System.out.println(msg);
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.setChannel(msg.substring(0, 1));
		messageEntity.setBody(msg.substring(1,msg.length()));
		return messageEntity;

	}
}
