package org.flowertwig.droidproxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;

public class Proxy extends Service implements Runnable {
	
	static final int PORT = 8080;
	
	private Thread proxy = null;
	private boolean stop = false;

	public final IBinder onBind(final Intent intent) {
		return null;
	}
	
	@Override
	public final void onCreate() {
		super.onCreate();
		
        Log.i(getClass().getSimpleName(), "Service Created");
	}
	
	@Override
	public final void onStart(final Intent intent, final int startId) {
		super.onStart(intent, startId);
				
        Log.i(getClass().getSimpleName(), "Service Start()");
        
        // Add Handler Init here
        Resources res = getResources();
        
        DebugHandler.Init(res);
        DroidProxyHandler.Init(res);
        LogHandler.Init(res);
        CacheHandler.Init(res);
        DebugHandler.WriteLog("*** All handlers initilized.");
        
        Log.i(getClass().getSimpleName(), "if (this.proxy == null)");
		if (this.proxy == null) {
	        Log.i(getClass().getSimpleName(), "proxy = null");
			// Toast.makeText(this, "starting proxy on port: " + this.port,
			// Toast.LENGTH_SHORT).show();
			final Thread pr = new Thread(this);
			pr.start();
			this.proxy = pr;
		} else {
	        Log.i(getClass().getSimpleName(), "proxy != null");
		}
	}

	@Override
	public final void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, R.string.proxy_stopped, Toast.LENGTH_LONG).show();
        Log.i(getClass().getSimpleName(), "OnDestroy");
		this.stop = true;
	}
	
	public void run() {
		try {
			//Toast.makeText(this, "TESTING", Toast.LENGTH_LONG).show();
			
	        Log.i(getClass().getSimpleName(), "Service run()");
			int p = PORT;
			ServerSocket sock = new ServerSocket(p);
			Socket client;
	        Log.i(getClass().getSimpleName(), "Service run(Using port=" + p + ")");

	        DebugHandler.WriteLog("*** Waiting for incomming connections.");
	        
			while (!this.stop) {
		        Log.i(getClass().getSimpleName(), "Waiting for connection");
				client = sock.accept();
		        Log.i(getClass().getSimpleName(), "Connection Accepted");
		        DebugHandler.WriteLog("*** New connection accepted.");
				if (client != null) {
					Log.d(getClass().getSimpleName(), "new client");
					Thread t = new Thread(new Connection(client));
					t.start();
				}
			}
			sock.close();
		} catch (IOException e) {
			Log.e(getClass().getSimpleName(), null, e);
		}
	}

}
