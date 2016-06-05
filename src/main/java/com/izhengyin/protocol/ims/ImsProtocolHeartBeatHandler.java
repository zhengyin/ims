package com.izhengyin.protocol.ims;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ImsProtocolHeartBeatHandler extends ChannelInboundHandlerAdapter{
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ImsMessage imsMessage = (ImsMessage)msg;
		if(imsMessage.getType() == ImsMessagType.SEND_HEARTBEAT){
			//响应客户端心跳
			imsMessage.setType(ImsMessagType.RESPONSE_HEARTBEAT);
			ctx.writeAndFlush(msg);
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	
}
