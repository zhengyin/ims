package com.izhengyin.protocol.websocket;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketMessageDecode extends MessageToMessageDecoder<WebSocketFrame>{
	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
		
		if(frame instanceof TextWebSocketFrame != true){
			throw new Exception("websocket 解码异常");
		}
		frame = (TextWebSocketFrame) frame;
		ByteBuf inBuf = frame.content();
		byte[] bytes = new byte[inBuf.readableBytes()];
		inBuf.getBytes(0, bytes);
		System.out.println("read bytes");
		for(int i = 0; i<bytes.length;i++){
			System.out.print(bytes[i]+" ");
		}
		System.out.println("\nread bytes");
		WebSocketMessage wsMessage = getWebSocketMessage(inBuf);
		out.add(wsMessage);
	}
	
	
	private WebSocketMessage getWebSocketMessage(ByteBuf in){
		
		WebSocketMessage wsMessage = new WebSocketMessage();
		
		wsMessage.setType(in.readByte());
		
		switch (wsMessage.getType()) {
			case WebSocketMessagType.AUTH:
				byte[] token = new byte[8];
				in.readBytes(token);
				wsMessage.setToken(token);
				break;
			case WebSocketMessagType.AUTH_FAIL:
				
				break;
			case WebSocketMessagType.AUTH_SUCCESS:
				
				break;
			case WebSocketMessagType.SEND_HEARTBEAT:
			
				break;
			case WebSocketMessagType.RESPONSE_HEARTBEAT:
				
				break;
			case WebSocketMessagType.NORMAL:
				if(in.readableBytes()>0){
					byte[] body = new byte[in.readableBytes()];
					in.readBytes(body);
					wsMessage.setBody(body);
				}
				break;
		}
		return wsMessage;
	}
}