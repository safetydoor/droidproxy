package org.flowertwig.droidproxy;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.content.res.Resources;

public class DroidProxyHandler {
//	private static int _requestMethodCall = 0; 
//	private static int _responseMethodCall = 0;
//	private static String _directory;
	
	public static void Init(final Resources res)
	{
//		_directory =res.getString(R.string.parent_directory) + res.getString(R.string.log_directory);
//        File droidProxyDir = new File(_directory);
//        try{
//        if (droidProxyDir.mkdirs())
//        {
//        	// TODO: Do something if we can't create directory.
//        }
//        }catch (java.lang.SecurityException sex)
//        {
//        	// TODO: Do something if we can't create directory.
//        }
//        droidProxyDir = null;
	}
	
	public static boolean HandleRequest(final RequestInfo req, OutputStream outStream)
	{
		//final String welcomeHTML = "HTTP/1.0 200 OK\r\nDate: Fri, 7 May 2010 22:00:00 GMT\r\nContent-Type: text/html\r\nContent-Length: 199\r\n\r\n<html><head><title>droidProxy - Välkommen</title></head><body><h1>droidProxy är nu installerat!</h1>Du kan hitta filer och inställningar för droidProxy under: /sdcard/droidProxy/</body></html>\r\n";
//		final String welcomeHTML = "HTTP/1.1 200 OK\r\nDate: Fri, 7 May 2010 22:00:00 GMT\r\nContent-Type: text/html\r\nContent-Length: 193\r\n\r\n<html><head><title>droidProxy - Välkommen</title></head><body><h1>droidProxy är nu installerat!</h1>Du kan hitta filer och inställningar för droidProxy under: /sdcard/droidProxy/</body></html>";
//		
//		try {
//			outStream.write(welcomeHTML.getBytes());
//			outStream.flush();
//			DebugHandler.WriteLog("*** DroidProxyHandler sent data to client.");
//			return true;
//		} catch (IOException e) {
//			DebugHandler.WriteLog("*** DroidProxyHandler encountered an exception while writing.");
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//		}
		
		// Log request
//		int call = _requestMethodCall++;
//		
//		String filename = _directory + "request-" + call + ".log";
//		FileHandler.Write(filename, req.getRaw());
					
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
