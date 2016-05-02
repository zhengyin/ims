package com.izhengyin.ims;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	int port = 8080;
        try {
			new Servers().run(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
