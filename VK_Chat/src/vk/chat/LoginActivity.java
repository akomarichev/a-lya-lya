package vk.chat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.json.JSONException;

import vk.api.API;
import vk.constants.Constants;
import vk.pref.Pref;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private API api;
	private Typeface tf;
	
	private Button b_login;
	private ImageView iv_logo;
	private ImageView iv_login;
	private ImageView iv_pass;
	private EditText et_login;
	private EditText et_password;
	
	private RelativeLayout layout;
	
	private HashMap<String, String> auth;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		api = new API(Constants.ACCESS_TOKEN);
		
		setupUI();
		setupFonts();	
	}
	
	private void setupUI(){
		b_login = (Button) findViewById(R.id.button1);
		b_login.setOnClickListener(b_login_listener);
		iv_logo = (ImageView) findViewById(R.id.imageView1);
		iv_login = (ImageView) findViewById(R.id.imageView2);
		iv_pass = (ImageView) findViewById(R.id.imageView3);
		et_login = (EditText) findViewById(R.id.editText1);
		et_login.setOnFocusChangeListener(et_loginClick);
		et_password = (EditText) findViewById(R.id.editText2);
		et_password.setOnFocusChangeListener(et_passwordClick);
		layout = (RelativeLayout) findViewById(R.id.relativeLayout);
		layout.setOnClickListener(layoutClick);
	}
	
	private void setupFonts(){
		tf = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");
		b_login.setTypeface(tf);
		et_login.setTypeface(tf);
		et_password.setTypeface(tf);
	}
	
	private OnClickListener b_login_listener=new OnClickListener(){
        @Override
        public void onClick(View v) {
        	try {
				auth = api.authorizationHTTPS(et_login.getText().toString(), et_password.getText().toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
        	
        	//Log.d("loginActivity",auth.toString());
        	if(auth==null){
        		Toast.makeText(getApplicationContext(), getResources().getString(R.string.auth_error_login_info), Toast.LENGTH_SHORT).show();
        	}else{
        		Pref.logIn(LoginActivity.this);
        		Pref.saveHTTPSAuth(LoginActivity.this, auth.get(Constants.ACCESS_TOKEN), auth.get(Constants.USER_ID));
        		startActivity(new Intent(LoginActivity.this, VK_ChatActivity.class));
        		finish();
        	}
        }
    };
	
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
	
	private OnClickListener layoutClick=new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			startActivity(new Intent(LoginActivity.this, SignupActivity.class));
			finish();
	    }
	};
}
