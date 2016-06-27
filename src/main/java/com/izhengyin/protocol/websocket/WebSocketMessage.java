package com.izhengyin.protocol.websocket;

import java.util.Arrays;

public class WebSocketMessage {
	private byte type;
	private byte[] token;
	private byte[] body;
	
	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * @return the token
	 */
	public byte[] getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(byte[] token) {
		this.token = token;
	}

	/**
	 * @return the body
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebSocketMessage [type=" + type + ", token=" + Arrays.toString(token) + ", body="
				+ Arrays.toString(body) + "]";
	}
	
}
