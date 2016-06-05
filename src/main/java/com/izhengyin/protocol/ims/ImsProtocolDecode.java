package com.izhengyin.protocol.ims;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
/**
 * Ims 协议解码器
 * @author zhengyin <zhengyin.name@gmail.com>
 *
 */
public class ImsProtocolDecode extends LengthFieldBasedFrameDecoder{

	public ImsProtocolDecode(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength,-7,0);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if(frame == null){
			return null;
		}
	
		ImsMessage imsMessage = getImsMessage(frame);
		
		System.out.println("receive msg:"+imsMessage.toString());
		
		return imsMessage;
	}
	
	private ImsMessage getImsMessage(ByteBuf in){
		
		ImsMessage imsMessage = new ImsMessage();
		
		byte[] crc = new byte[2];
		
		in.readBytes(crc);
		
		imsMessage.setCrc(crc);
	
		imsMessage.setType(in.readByte());
		
		imsMessage.setLength(in.readInt());
		
		switch (imsMessage.getType()) {
			case ImsMessagType.AUTH:
				byte[] token = new byte[8];
				in.readBytes(token);
				imsMessage.setToken(token);
				break;
			case ImsMessagType.AUTH_FAIL:
				
				break;
			case ImsMessagType.AUTH_SUCCESS:
				
				break;
			case ImsMessagType.SEND_HEARTBEAT:
			
				break;
			case ImsMessagType.RESPONSE_HEARTBEAT:
				
				break;
			case ImsMessagType.NORMAL:
				//继续读取body
				if(in.readableBytes()>0){
					byte[] body = new byte[in.readableBytes()];
					in.readBytes(body);
					imsMessage.setBody(body);
				}
				break;
		}
		
		return imsMessage;
	}
	
}
