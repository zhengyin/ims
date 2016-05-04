package com.izhengyin.privateProtocol;

public class MessageEntity {
	private String C;
	private String B;
	/**
	 * @return the c
	 */
	public String getC() {
		return C;
	}
	/**
	 * @param c the c to set
	 */
	public void setC(String c) {
		C = c;
	}
	/**
	 * @return the b
	 */
	public String getB() {
		return B;
	}
	/**
	 * @param b the b to set
	 */
	public void setB(String b) {
		B = b;
	}
	
	@Override
	public String toString() {
		return getC()+getB();
	}
	
}
