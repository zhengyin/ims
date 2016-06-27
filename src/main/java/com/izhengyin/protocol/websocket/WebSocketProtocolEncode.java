package com.izhengyin.protocol.websocket;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketProtocolEncode extends MessageToMessageEncoder<WebSocketMessage>{

	private static final byte CRC1 = 73;
	private static final byte CRC2 = 77;
	@Override
	protected void encode(ChannelHandlerContext ctx, WebSocketMessage msg, List<Object> out) {
		
		ByteBuf sendBuf = Unpooled.buffer();
		
		try {
			
			System.out.println("sendBuf:"+sendBuf.readableBytes()+" msg:"+msg.toString());
			
			setHeader(msg, sendBuf);
			
			if(msg.getBody() != null && msg.getBody().length>0){
				sendBuf.writeBytes(msg.getBody());
			}
			
			TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(sendBuf);
			
			out.add(textWebSocketFrame);
			
		} catch (Exception e) {
			//编码异常关闭连接
			ctx.close();
			
			e.printStackTrace();
			
		}
	}
	
	
	private void setHeader(WebSocketMessage msg,ByteBuf out) throws Exception{
		
		//校验消息类型
		if(!checkType(msg.getType())){
			throw new Exception("消息类型错误");
		}
		
		//写入消息类型
		out.writeByte(msg.getType());
		
		switch (msg.getType()) {
			case WebSocketMessagType.AUTH:
				
				break;
			case WebSocketMessagType.AUTH_FAIL:
			
				break;
			case WebSocketMessagType.AUTH_SUCCESS:
				
				break;
			case WebSocketMessagType.SEND_HEARTBEAT:
			
				break;
			case WebSocketMessagType.RESPONSE_HEARTBEAT:
				
				break;				
			}
	}
	
	private boolean checkType(byte type){
		
		if(
				type == WebSocketMessagType.AUTH ||
				type == WebSocketMessagType.AUTH_FAIL ||
				type == WebSocketMessagType.AUTH_SUCCESS ||
				type == WebSocketMessagType.NORMAL ||
				type == WebSocketMessagType.RESPONSE_HEARTBEAT ||
				type == WebSocketMessagType.SEND_HEARTBEAT 
		){
			return true;
		}else{
			return false;
		}
	}
}
