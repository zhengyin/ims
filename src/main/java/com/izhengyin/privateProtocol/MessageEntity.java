package com.izhengyin.privateProtocol;

import java.util.Arrays;

public class MessageEntity {

	private MessageHeaderEntity header;
	private byte[] body;
	public MessageHeaderEntity getHeader() {
		return header;
	}
	public void setHeader(MessageHeaderEntity header) {
		this.header = header;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "MessageEntity [header=" + header + ", body=" + Arrays.toString(body) + "]";
	}
}
