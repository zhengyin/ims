package com.izhengyin.protocol.websocket;

public class WebSocketMessagType {
	public static final byte AUTH = 49;						//认证
	public static final byte AUTH_FAIL = 50;				//认证失败
	public static final byte AUTH_SUCCESS = 51;				//认证成功
	public static final byte SEND_HEARTBEAT = 52;			//发送心跳
	public static final byte RESPONSE_HEARTBEAT = 53;		//服务端响应心跳
	public static final byte NORMAL = 54;					//正常的消息传输
}
