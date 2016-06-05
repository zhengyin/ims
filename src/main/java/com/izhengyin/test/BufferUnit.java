package com.izhengyin.test;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BufferUnit {
	private static final Logger logger = LoggerFactory.getLogger(BufferUnit.class);
	
	
	public static ByteBuffer ensureWritable(ByteBuffer buffer, int needSize){
		bufferInfo(buffer, "in buffer");
		ByteBuffer tmpBuffer = ByteBuffer.allocate(needSize);
		buffer.flip();
		tmpBuffer.put(buffer);
		buffer = tmpBuffer;
		bufferInfo(buffer, "out buffer");
		return buffer;
	}
	
	public static void bufferInfo(ByteBuffer buf,String info){
		logger.debug("============"+info+"==========");
		logger.debug("position:"+buf.position());
		logger.debug("limit:"+buf.limit());
		logger.debug("capacity:"+buf.capacity());
		
	}
}
