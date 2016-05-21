package com.izhengyin.privateProtocol;

public class MessageEntity {
	private String channel;
	private String body;
	
	
	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	@Override
	public String toString() {
		return getChannel()+getBody();
	}
}
