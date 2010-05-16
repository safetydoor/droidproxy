package org.flowertwig.droidproxy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;

import android.util.Log;

public class RequestInfo {
	
	// YES, this IS ugly but a HTTP header shouldn't be bigger the 4K..
	private static final int HEADER_BUFFER_SIZE = 4096;
	
	protected String _method;
	protected String _address;
	protected String _host;
	protected int _hostPort = -1;
	protected byte[] _raw;
	protected int _length;
	
	public void setMethod(final String method) { _method = method; }
	public String getMethod() { return _method; }
	public void setAddress(final String addr) { _address = addr; }
	public String getAddress() { return _address; }
	public void setHost(final String host) { _host = host; }
	public String getHost() { return _host; }
	public void setHostPort(final int port) { _hostPort = port; }
	public int getHostPort() { if (_hostPort == -1) return 80; return _hostPort; }
	public void setRaw(final byte[] raw) { _raw = raw; }
	public byte[] getRaw() { return _raw; }
	public void setRawLength(final int length) { _length = length; }
	public int getRawLength() { if (_length < 0) return 0; return _length; }
	
	public boolean isValid() { return (_method != null && _address != null && _host != null && _raw != null); }

	public RequestInfo() { }
	public RequestInfo(final byte[] raw, final int length)
	{
		setRaw(raw);
	}
	
	public RequestInfo(final String method, final String addr, final String host, final byte[] raw)
	{
		setMethod(method);
		setAddress(addr);
		setHost(host);
		setRaw(raw);
	}
	
	public static RequestInfo Parse(final BufferedInputStream inputStream)
	{
		// YES, this IS ugly but a HTTP header shouldn't be bigger the 4K...
		String strHeader = null;
		int available;
		byte[] buffer = new byte[HEADER_BUFFER_SIZE];
		RequestInfo info = new RequestInfo();
		try {
			Log.i("droidProxy", "Before Parse.avaliable");

			DebugHandler.WriteLog("*** Start reading incomming data.");

			int loopIndex = 0;
			while(true)
			{
				available = inputStream.read(buffer, 0, HEADER_BUFFER_SIZE);
				
				if (available == -1)
				{
					DebugHandler.WriteLog("*** No more data to receive on connection.");
					Log.i("droidProxy", "Done Parse.avaliable");
					break;
				}
				
				Log.i("droidProxy", "After Parse.avaliable - Success");
				
				// Expand buffer if needed.
				if (info.getRawLength() != 0)
				{
					DebugHandler.WriteLog("*** More data has been received then the default buffer allows (Expanding buffer).");
					
					int prevLength = info.getRawLength();
					int totalLength = prevLength + available;
					byte[] destBuff = new byte[totalLength];
					System.arraycopy(info.getRaw(), 0, destBuff, 0, prevLength);
					System.arraycopy(buffer, 0, destBuff, prevLength, available);
					buffer = destBuff;
					destBuff = null;

					available = totalLength;
				}
				
				info.setRaw(buffer);
				info.setRawLength(available);
				
				DebugHandler.WriteLog("*** Reading loop has looped " + loopIndex + " times.");
				loopIndex++;
			}	

			
			DebugHandler.WriteLog("*** Start parsing " + info.getRawLength() + " of received bytes.");
			
			// We just want todo this one time so we will collect all buffer first.
			strHeader = new String(info.getRaw(), 0, info.getRawLength());
			
			DebugHandler.WriteLog("*** Request: \r\n" + strHeader);
			
			String[] lines = strHeader.split("\n");
			// Get the method call.
			if (lines.length > 0)
			{
				String[] sections = lines[0].split(" ");
				// Is it a valid HTTP Request? (http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5)
				if (sections.length == 3)
				{
					info.setMethod(sections[0]);
					info.setAddress(sections[1]);
				}
			}
			
			// Get all other values in HTTP header.
			for(int i = 0; i < lines.length; i++)
			{
				String line = lines[i];
				int index;
				if ((index = line.indexOf(":")) > 0)
				{
					String key = line.substring(0, index).toLowerCase();
					String value = line.substring(++index).trim();
					if (key.equals("host"))
					{
						info.setHost(value);
					}
				}
			}
			
			return info;
		} catch (IOException e) 
		{
			Log.e("droidProxy", "Parse exception: " + e.toString());
		}
		return null;
	}
}
