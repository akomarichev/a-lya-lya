package vk.chat;

import java.io.IOException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;

import vk.api.API;
import vk.api.User;
import vk.utils.Synch;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class HandsetContactsActivity extends ActivityGroup {
	protected static LocalActivityManager mLocalActivityManager;
	Button launchButton;
	Boolean press_synch;
	
	/** Called when the activity is first created. */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.synch_contacts);
		
		SharedPreferences prefs = getSharedPreferences("Synch", MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		final Editor editor=prefs.edit();
		press_synch = prefs.getBoolean("press_synch", false);		
		
		if(press_synch){
			replaceContentView("SynchActivity", new Intent(this, SynchActivity.class));
		}
		else{
			launchButton = (Button)findViewById(R.id.b_synch_cont);
			launchButton.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v) {
					editor.putBoolean("press_synch", true).commit();
					Intent activity3Intent = new Intent(v.getContext(), SynchActivity.class);
					replaceContentView("activity3", activity3Intent);					
					}
			});
		}
	}
		
	public void replaceContentView(String id, Intent newIntent) {			
		View view = getLocalActivityManager().startActivity(id,newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)) .getDecorView(); 			
		this.setContentView(view);
			}        
	}
/*public class HandsetContactsActivity extends Activity {
	private ArrayList<User> users = new ArrayList();
	private static User [] u;


	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    TextView textview = new TextView(this);
        textview.setText("This is the Handset Contacts tab");
        setContentView(textview);
        
        String phone = "";
        
        Map<String,String> list_phone = new HashMap();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
          String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));          
          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          if(phoneNumber.length() >= 11 && phoneNumber.length() <= 12){
        	  phone += phoneNumber+",";
        	  list_phone.put(phoneNumber, name);
          }
        }
        phones.close();
        phone = phone.substring(0, phone.length() - 1);
        
        /*for (Entry<String, String> entry : list_phone.entrySet()) {
            Log.d("key",entry.getKey());
            Log.d("value",entry.getValue());
        }
        Log.d("phone",phone);
        API api = new API();
        try {
			users = api.getUsersByPhones(phone);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    
	    
	    u = new User[users.size()];
		for(int i = 0; i<users.size(); i++ ){
			u[i]=users.get(i);	
		}
		
		Log.d("phone_users",u.toString());
        
		
	}
}*/
