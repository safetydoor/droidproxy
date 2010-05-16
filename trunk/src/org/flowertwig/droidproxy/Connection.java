package org.flowertwig.droidproxy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.content.res.Resources;
import android.util.Log;

public class Connection implements Runnable {

	private final static short STATE_NORMAL = 0;
	private final static short STATE_CLOSED_OUT = 2;

	private short state = STATE_NORMAL;
	private static final int BUFFER_SIZE = 32768;

	private Socket _localSocket;
	private Socket _remoteSocket;
	public Connection(Socket s) { this._localSocket = s; }

	/**
	 * Close local and remote socket.
	 * 
	 * @param nextState
	 *            state to go to
	 * @return new state
	 * @throws IOException
	 *             IOException
	 */
	private synchronized short close(final short nextState)
			throws IOException {
		Log.d(this.getClass().getSimpleName(), "close(" + nextState + ")");
		short mState = this.state;
		if (mState == STATE_NORMAL || nextState == STATE_NORMAL) {
			mState = nextState;
		}
		if (mState != STATE_NORMAL) {
			// close remote socket
			if (_remoteSocket != null && _remoteSocket.isConnected()) {
				_remoteSocket.close();
			}
			this._remoteSocket = null;
		}
		if (mState == STATE_CLOSED_OUT) {
			// close local socket
			if (_localSocket.isConnected()) {
				_localSocket.close();
			}
		}
		this.state = mState;
		return mState;
	}
	
	protected void getResponse(final RequestInfo request, OutputStream destinationStream)
	{
		try {
			InputStream rInStream = null;
			OutputStream rOutStream = null;
				
			if(_remoteSocket == null)
			{
				_remoteSocket = new Socket();
				_remoteSocket.connect(new InetSocketAddress(request.getHost(), request.getHostPort()));
			}
			rInStream = _remoteSocket.getInputStream();
			rOutStream = _remoteSocket.getOutputStream();
	
			// Write our request into stream.
			rOutStream.write(request.getRaw());
			rOutStream.flush();
			
			// Wait and write the response to our stream.
			int available;
			int pos = 0;
			
			byte[] buffer = new byte[BUFFER_SIZE];
			while (true)
			{
				available = rInStream.read(buffer, 0, BUFFER_SIZE);
				
				if (available == -1)
				{
					return;	// Cancel while loop
				}
				
				// Add response handling here
				boolean wasHandled = LogHandler.HandleReceive(request, destinationStream, buffer, pos, available);
				pos+= available;
				
				if (!wasHandled && destinationStream != null)
				{
					destinationStream.write(buffer, 0, available);
					destinationStream.flush();
				}
				// TODO: We are not handling https:// requests here.
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(getClass().getSimpleName(), "error during read" + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		BufferedInputStream inStream;
		OutputStream outStream;
		try {

			inStream = new BufferedInputStream(_localSocket.getInputStream(), BUFFER_SIZE);
			outStream = _localSocket.getOutputStream();
			
			while (_localSocket.isConnected())
			{
				boolean shouldSendRequest = true;
				RequestInfo req = RequestInfo.Parse(inStream);
				if (req == null || !req.isValid())
				{
					// Something went terrible wrong. Close connection 
					Log.d(this.getClass().getSimpleName(), "shutdown socket (Unable to parse)");
					shouldSendRequest = false;
					
					DebugHandler.WriteLog("*** No valid request for this connection.");

					// TODO: Add HTTP Error response (5xx).
				}
				else
				{
					DebugHandler.WriteLog("*** Request for this connection seems to be valid.");
					
					// Do special stuff in our handlers
					shouldSendRequest = !DroidProxyHandler.HandleRequest(req, outStream);
					if (shouldSendRequest)
					{
						DebugHandler.WriteLog("*** droidProxyHandler didn't want to handle the request.");
						shouldSendRequest = !LogHandler.HandleRequest(req, outStream);
						if (shouldSendRequest)
						{
							DebugHandler.WriteLog("*** LogHandler wanted to override the response.");
							shouldSendRequest = !CacheHandler.HandleRequest(req, outStream);
						}
						else
						{
							DebugHandler.WriteLog("*** LogHandler didn't want to override the response.");
						}
					}
				}
				
				if (shouldSendRequest)
				{
					getResponse(req, outStream);
				}
			}
			this.close(STATE_CLOSED_OUT);
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(), null, e);
			return;
		}
		Log.d(this.getClass().getSimpleName(), "close connection");
	}
}
