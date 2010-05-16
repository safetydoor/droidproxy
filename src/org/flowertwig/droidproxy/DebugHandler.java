package org.flowertwig.droidproxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.res.Resources;

public class DebugHandler
{
	private static String _directory;
	private static FileOutputStream _debugStream;
	
	public static void Init(final Resources res) {
		_directory =res.getString(R.string.parent_directory);
		
        final File droidProxyDir = new File(_directory);
        try{
	        if (!droidProxyDir.mkdirs())
	        {
	        	// TODO: Do something if we can't create directory.
	        }
            try {
				_debugStream = new FileOutputStream(_directory + "debug.log");
				WriteLog("*** DebugHandler has been initilized.");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }catch (java.lang.SecurityException sex)
        {
        	// TODO: Do something if we can't create directory.
        }
	}
	
	public static void WriteLog(String msg)
	{
		try {
			_debugStream.write((msg + "\r\n").getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
