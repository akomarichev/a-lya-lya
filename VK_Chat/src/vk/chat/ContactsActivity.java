package vk.chat;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

public class ContactsActivity extends TabActivity {
	
	TabHost tabHost;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contacts);   
        
        Log.d("Artem","contacts");
	    
	    Resources res = getResources(); // Resource object to get Drawables
        tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        
        //tabHost.setOnTabChangedListener(this);

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, FriendsActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("friends").setIndicator(getString(R.string.c_friends))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, OnlineFriendsActivity.class);
        spec = tabHost.newTabSpec("online_friends").setIndicator(getString(R.string.c_online))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, HandsetContactsActivity.class);
        spec = tabHost.newTabSpec("handset_contacts").setIndicator(getString(R.string.c_contacts))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_black_bottom);
        }
	}

}
