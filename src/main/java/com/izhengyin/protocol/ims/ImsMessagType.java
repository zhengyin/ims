package com.izhengyin.protocol.ims;

public class ImsMessagType {
	public static final byte AUTH = 1;						//认证
	public static final byte AUTH_FAIL = 2;				//认证失败
	public static final byte AUTH_SUCCESS = 3;				//认证成功
	public static final byte SEND_HEARTBEAT = 4;			//发送心跳
	public static final byte RESPONSE_HEARTBEAT = 5;//服务端响应心跳
	public static final byte NORMAL = 6;					//正常的消息传输
}
