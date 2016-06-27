package com.izhengyin.protocol.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class WebSocketProtocolHeartBeatHandler extends ChannelInboundHandlerAdapter{
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		WebSocketMessage wsMessage = (WebSocketMessage)msg;
		if(wsMessage.getType() == WebSocketMessagType.SEND_HEARTBEAT){
			//响应客户端心跳
			wsMessage.setType(WebSocketMessagType.RESPONSE_HEARTBEAT);
			ctx.writeAndFlush(msg);
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	
}
