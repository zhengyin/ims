package com.izhengyin.protocol.ims;

import java.util.Arrays;

public class ImsMessage {
	private byte[] crc ;
	private byte type;
	private int length;
	private byte[] token;
	private byte[] body;
	/**
	 * @return the crc
	 */
	public byte[] getCrc() {
		return crc;
	}
	/**
	 * @param crc the crc to set
	 */
	public void setCrc(byte[] crc) {
		this.crc = crc;
	}
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
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
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
		return "ImsMessage [crc=" + Arrays.toString(crc) + ", type=" + type + ", length=" + length + ", token=" + Arrays.toString(token)
				+ ", body=" + Arrays.toString(body) + "]";
	}
}
