package com.izhengyin.protocol.ims;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class ImsProtocolEncode extends MessageToMessageEncoder<ImsMessage>{

	private static final byte CRC1 = 73;
	private static final byte CRC2 = 77;
	@Override
	protected void encode(ChannelHandlerContext ctx, ImsMessage msg, List<Object> out) {
		
		ByteBuf sendBuf = Unpooled.buffer();
		
		try {
			setHeader(msg, sendBuf);
			
			if(msg.getBody() != null && msg.getBody().length>0){
				sendBuf.writeBytes(msg.getBody());
			}
			
			sendBuf.setInt(3, sendBuf.readableBytes());
			
			System.out.println("sendBuf:"+sendBuf.readableBytes()+" msg:"+msg.toString());
			
			out.add(sendBuf);
			
		} catch (Exception e) {
			
			//编码异常关闭连接
			ctx.close();
			
			e.printStackTrace();
		}
		
	}
	
	
	private void setHeader(ImsMessage msg,ByteBuf out) throws Exception{
		
		//校验消息类型
		if(msg.getType() <= 0 || msg.getType()>6){
			throw new Exception("消息类型错误");
		}
		
		//认证消息需要附加token
		if(msg.getType() == ImsMessagType.AUTH && msg.getToken().length<=0){
			throw new Exception("认证消息缺少token");
		}
		
		//写入协议检验码
		out.writeByte(CRC1);
		out.writeByte(CRC2);
		
		//写入消息类型
		out.writeByte(msg.getType());
		
		//写入消息长度
		out.writeInt(7);
		
		switch (msg.getType()) {
			case ImsMessagType.AUTH:
				byte[] token = msg.getToken();
				out.writeBytes(token);
				break;
			case ImsMessagType.AUTH_FAIL:
			
				break;
			case ImsMessagType.AUTH_SUCCESS:
				
				break;
			case ImsMessagType.SEND_HEARTBEAT:
			
				break;
			case ImsMessagType.RESPONSE_HEARTBEAT:
				
				break;				
			}
	}
}
