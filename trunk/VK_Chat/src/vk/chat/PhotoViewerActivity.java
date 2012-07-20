package vk.chat;

import vk.adapters.ImageDownloader;
import vk.utils.CheckConnection;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoViewerActivity extends Activity {
	private TextView photo_name;
	private ImageView photo;
	
	private String phototext = "";
	private String src = "";
	
	private ImageDownloader loader ;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_viewer_big);
		loader = new ImageDownloader();
		
		checkExtras();
		
		photo_name = (TextView) findViewById(R.id.photo_name);
		if(!phototext.equals("") || phototext != null)
			photo_name.setText(phototext);
		else
			photo_name.setText("Unknown Photo");
		
		photo = (ImageView) findViewById(R.id.photo);
		if(!src.equals("") || src!=null){
			if(CheckConnection.isOnline(this))
				loader.download(src, photo);
			else 
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.check_connect), Toast.LENGTH_LONG).show();
		}
	}
	
	protected void checkExtras() {
		Intent i = getIntent();
		Bundle extras = getIntent().getExtras();
		if(i.hasExtra("photo_name")){
			phototext = extras.getString("photo_name");
		}
		Log.d("phototext", phototext);
		if(i.hasExtra("photo")){
			src = extras.getString("photo");
		}
		Log.d("src", src);
	}

}
