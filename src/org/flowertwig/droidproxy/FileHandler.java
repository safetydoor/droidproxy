package org.flowertwig.droidproxy;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {

	public static void Write(String filename, byte[] data)
	{
		Write(filename, data, 0, data.length);	
	}
	public static void Write(String filename, byte[] data, int offset, int length)
	{
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(data, offset, length);
			fos.flush();
			fos.close();
			fos = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
