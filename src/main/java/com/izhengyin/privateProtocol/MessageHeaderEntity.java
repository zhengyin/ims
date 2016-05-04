package com.izhengyin.privateProtocol;

public class MessageHeaderEntity {
	private int crcCode;
	private int length;
	private long sessionId;
	private int type;
	public int getCrcCode() {
		return crcCode;
	}
	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "MessageHeaderEntity [crcCode=" + crcCode + ", length=" + length + ", sessionId=" + sessionId + ", type="
				+ type + "]";
	}
}
