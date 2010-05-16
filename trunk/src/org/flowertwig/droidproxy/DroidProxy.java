package org.flowertwig.droidproxy;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

public class DroidProxy extends Activity {
	
//	@Override
//	public void onResume()
//	{
//		super.onResume();
		
//        final Resources _resources = this.getResources();
//        File droidProxyDir = new File(_resources.getString(R.string.parent_directory));
//        try{
//        if (droidProxyDir.mkdirs())
//        {
//        	// TODO: Do something if we can't create directory.
//        }
//        }catch (java.lang.SecurityException sex)
//        {
//        	// TODO: Do something if we can't create directory.
//        }
//        
//        setContentView(R.layout.main);
//        
//        Log.i(getClass().getSimpleName(), "Before Start Service");
//        this.startService(new Intent(this, Proxy.class));
//        Log.i(getClass().getSimpleName(), "After Start Service");
//	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Resources _resources = this.getResources();
        File droidProxyDir = new File(_resources.getString(R.string.parent_directory));
        try{
        if (droidProxyDir.mkdirs())
        {
        	// TODO: Do something if we can't create directory.
        }
        }catch (java.lang.SecurityException sex)
        {
        	// TODO: Do something if we can't create directory.
        }
        
        setContentView(R.layout.main);
        
        Log.i(getClass().getSimpleName(), "Before Start Service");
        this.startService(new Intent(this, Proxy.class));
        Log.i(getClass().getSimpleName(), "After Start Service");
    }
}