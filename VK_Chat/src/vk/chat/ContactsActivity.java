package vk.chat;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class ContactsActivity extends TabActivity{
	
	TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contacts);   
	    
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        Intent intent;

        intent = new Intent().setClass(this, FriendsActivity.class);
        setupTab(new TextView(this), getString(R.string.c_friends), intent);

        intent = new Intent().setClass(this, OnlineFriendsActivity.class);
        setupTab(new TextView(this), getString(R.string.c_online), intent);

        intent = new Intent().setClass(this, HandsetContactsActivity.class);
        setupTab(new TextView(this), getString(R.string.c_contacts), intent);
	}
	
	private void setupTab(final View view, final String tag, final Intent intent) {
		View tabview = createTabView(tabHost.getContext(), tag);
	        TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_contacts, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

}
