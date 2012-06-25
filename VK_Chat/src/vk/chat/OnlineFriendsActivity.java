package vk.chat;

import vk.utils.Synch;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.TextView;

public class OnlineFriendsActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    TextView textview = new TextView(this);
        textview.setText("This is the Online Friends tab");
        setContentView(textview);
        
        /*SharedPreferences prefs = getSharedPreferences("Synch", MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.remove("press_synch").commit();
        editor.commit();*/
	}

}
