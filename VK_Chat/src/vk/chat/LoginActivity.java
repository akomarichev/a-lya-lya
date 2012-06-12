package vk.chat;

import vk.api.API;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends Activity {
	API api;
	Typeface tf;
	
	Button b_login;
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
		//response = api.SendHttpPost();
		Log.d("Artem", response);
		
		setupUI();
		setupFonts();	
	}
	
	private void setupUI(){
		b_login = (Button) findViewById(R.id.button1);
		iv_logo = (ImageView) findViewById(R.id.imageView1);
		iv_login = (ImageView) findViewById(R.id.imageView2);
		iv_pass = (ImageView) findViewById(R.id.imageView3);
		et_login = (EditText) findViewById(R.id.editText1);
		et_login.setOnFocusChangeListener(et_loginClick);
		et_password = (EditText) findViewById(R.id.editText2);
		et_password.setOnFocusChangeListener(et_passwordClick);
	}
	
	private void setupFonts(){
		b_login.setTypeface(tf);
		et_login.setTypeface(tf);
		et_password.setTypeface(tf);
	}
	
	private OnFocusChangeListener et_loginClick=new View.OnFocusChangeListener(){
		@Override
	    public void onFocusChange(View v, boolean hasFocus) {
	        if (hasFocus) {
	            iv_login.setImageResource(R.drawable.login_phone_active);
	        }
	        else
	        	iv_login.setImageResource(R.drawable.login_phone);
	    }
	};
	
	private OnFocusChangeListener et_passwordClick=new View.OnFocusChangeListener(){
		@Override
	    public void onFocusChange(View v, boolean hasFocus) {
	        if (hasFocus) {
	        	iv_pass.setImageResource(R.drawable.login_pass_active);
	        }
	        else
	        	iv_pass.setImageResource(R.drawable.login_pass);
	    }
	};
}
