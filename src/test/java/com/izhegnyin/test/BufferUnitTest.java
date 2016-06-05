package com.izhegnyin.test;

import java.nio.ByteBuffer;

import com.izhengyin.test.BufferUnit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
public class BufferUnitTest extends TestCase{
	public BufferUnitTest(String name) {
		super(name);
	}
	 /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BufferUnitTest.class );
    }
    
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testEnsureWritable(){
    	ByteBuffer buffer = ByteBuffer.allocate(12);
    	String s1 = "test Ensure";
    	byte[] b1 = s1.getBytes();
    	buffer.put(b1);
    	String s2 = "Writable";
    	byte[] b2 = s2.getBytes();
    	int needSize = b1.length+b2.length;
    	buffer = BufferUnit.ensureWritable(buffer, needSize);
    	buffer.flip();
    	assertTrue(buffer.capacity() == needSize);
    }
}
