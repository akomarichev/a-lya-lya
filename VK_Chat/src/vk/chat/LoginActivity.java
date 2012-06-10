package vk.chat;

import org.json.JSONObject;

import vk.api.API;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends Activity {
	API api;
	Typeface tf;
	
	Button b;
	ImageView iv_logo;
	ImageView iv_login;
	ImageView iv_pass;
	EditText et_login;
	EditText et_password;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		tf = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");
		
		Log.d("Artem", "1");
		api = new API();
		String response = "";
		response = api.SendHttpPost();
		Log.d("Artem", response);
		
		setupUI();
		setupFonts();	
	}
	
	private void setupUI(){
		b = (Button) findViewById(R.id.button1);
		iv_logo = (ImageView) findViewById(R.id.imageView1);
		iv_login = (ImageView) findViewById(R.id.imageView2);
		iv_pass = (ImageView) findViewById(R.id.imageView3);
		et_login = (EditText) findViewById(R.id.editText1);
		et_password = (EditText) findViewById(R.id.editText2);
	}
	
	private void setupFonts(){
		b.setTypeface(tf);
		et_login.setTypeface(tf);
		et_password.setTypeface(tf);
	}
}
