package org.flowertwig.droidproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;

import android.content.res.Resources;

public class LogHandler {
	
	private static int _requestMethodCall = 0; 
	private static int _responseMethodCall = 0;
	private static String _directory;
	
	public static void Init(final Resources res)
	{
		_directory =res.getString(R.string.parent_directory) + res.getString(R.string.log_directory);
        File droidProxyDir = new File(_directory);
        try{
        if (droidProxyDir.mkdirs())
        {
        	// TODO: Do something if we can't create directory.
        }
        }catch (java.lang.SecurityException sex)
        {
        	// TODO: Do something if we can't create directory.
        }
        droidProxyDir = null;
	}
	
	public static boolean HandleRequest(final RequestInfo req, OutputStream outStream)
	{
		
		// Log request
		int call = _requestMethodCall++;
		
		String filename = _directory + "request-" + call + ".log";
		FileHandler.Write(filename, req.getRaw());
					
		// Log host
//		String filenameHost = _directory + "host-" + call + ".log";
//		byte[] data2 = (req.getHost() + ":" + req.getHostPort()).getBytes();
//		FileHandler.Write(filenameHost, data2);
//
//		// Log address
//		String filenameAddress = _directory + "address-" + call + ".log";
//		byte[] data3 = req.getAddress().getBytes();
//		FileHandler.Write(filenameAddress, data3);
		
		return false;
	}
	
	public static boolean HandleReceive(final RequestInfo req, OutputStream outStream, final byte[] buffer, final int offset, final int length)
	{
//		if (length > 0)
//		{
//			int call = _responseMethodCall++;
//			String filenameAddress = _directory + "response-" + call + ".log";
//			FileHandler.Write(filenameAddress, buffer, 0, length);
//		}
		return false;
	}
}
