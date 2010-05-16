package org.flowertwig.droidproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.res.Resources;

public class CacheHandler {
	
	private static int _requestMethodCall = 0; 
	private static String _directory;

	public static void Init(final Resources res)
	{
		_directory =res.getString(R.string.parent_directory) + res.getString(R.string.cache_directory);
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
		
		
		try {
						
			String filename = _directory + "cache-" + (_requestMethodCall++) + ".cache";
			
			FileOutputStream fos = new FileOutputStream(filename);
			byte[] data = req.getRaw();
			fos.write(data, 0, data.length);
			fos.flush();
			fos.close();
			fos = null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return false;
	}
	
	public static boolean HandleReceive(final RequestInfo req, OutputStream outStream, final byte[] buffer, final int offset, final int length)
	{
		
		return false;
	}
}
