package vk.chat;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import vk.api.API;
import vk.constants.Constants;
import vk.pref.Pref;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignupActivity extends Activity {	
	private ImageView iv_back;
	private API api;
	private Typeface tf;
	
	private ImageView checkPhone;
	private ImageView checkName;
	private ImageView checkSername;
	
	private EditText et_checkPhone;
	private EditText et_checkName;
	private EditText et_checkSername;
	
	private Button signup;
	
	private Boolean errorCheckPhone = true;
	private Boolean errorCheckName= true;
	private Boolean errorCheckSername = true;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		
		if(Pref.signedUp(SignupActivity.this)){
			startActivity(new Intent(SignupActivity.this, ConfirmActivity.class));
			finish();
		}
		else{
			api = new API(Constants.ACCESS_TOKEN);		
			setupUI();
			setupFonts();
		}
	}

	private void setupUI(){
		iv_back = (ImageView) findViewById(R.id.signup_back);
		iv_back.setOnClickListener(backClick);		
		
		checkPhone = (ImageView) findViewById(R.id.iv_signup_phone);
		checkPhone.setVisibility(View.GONE);
		checkName = (ImageView) findViewById(R.id.iv_signup_name);
		checkName.setVisibility(View.GONE);
		checkSername = (ImageView) findViewById(R.id.iv_signup_sername);
		checkSername.setVisibility(View.GONE);
		
		et_checkPhone = (EditText) findViewById(R.id.et_signup_phone);
		et_checkPhone.addTextChangedListener(et_checkPhoneListener);
		et_checkName = (EditText) findViewById(R.id.et_signup_name);
		et_checkName.addTextChangedListener(et_checkNameListener);
		et_checkSername = (EditText) findViewById(R.id.et_signup_sername);
		et_checkSername.addTextChangedListener(et_checkSernameListener);
		
		signup = (Button) findViewById(R.id.b_signup);
		signup.setOnClickListener(signupClick);
	}
	
	private void setupFonts(){
		tf = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");
		signup.setTypeface(tf);
		et_checkPhone.setTypeface(tf);
		et_checkName.setTypeface(tf);
		et_checkSername.setTypeface(tf);
	}
	
	private OnClickListener backClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(SignupActivity.this, LoginActivity.class));
    		finish();				
		}
	};
	
	private OnClickListener signupClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			if(errorCheckPhone)
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_phone_error), Toast.LENGTH_LONG).show();
			else if(errorCheckName)
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_name_error), Toast.LENGTH_LONG).show();
			else if(errorCheckSername)
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_sername_error), Toast.LENGTH_LONG).show();			
			else{
				int result = 0;
				try {
					result = api.signupPhone(et_checkPhone.getText().toString(), 
								et_checkName.getText().toString(),
								et_checkSername.getText().toString());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				if(result == 1){
					Pref.signUp(SignupActivity.this);
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_signup_ok), Toast.LENGTH_LONG).show();
		        	startActivity(new Intent(SignupActivity.this, ConfirmActivity.class));
					finish();
				} else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_signup_error), Toast.LENGTH_LONG).show();
				}
			}
		}
	};
	
	private TextWatcher et_checkPhoneListener = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			checkPhone(et_checkPhone.getText().toString());
		}		
	};
	
	private TextWatcher et_checkNameListener = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			checkName(et_checkName.getText().toString());
		}		
	};
	
	private TextWatcher et_checkSernameListener = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			checkSerName(et_checkSername.getText().toString());
		}		
	};
 
    private void checkPhone(final String phone) {
        new Thread(){
            @Override
            public void run(){
                try {
                	if(api.checkPhone(phone)==1)
                		runOnUiThread(checkPhoneOkRunnable);            	       
                	else
                		runOnUiThread(checkPhoneErrorRunnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    private void checkName(final String name) {
        new Thread(){
            @Override
            public void run(){
            	if(hasOnlyLetters(name))
            		runOnUiThread(checkNameOkRunnable);
            	else
            		runOnUiThread(checkNameErrorRunnable);
            }
        }.start();
    }
    
    private void checkSerName(final String serName) {
        new Thread(){
            @Override
            public void run(){
            	if(hasOnlyLetters(serName))
            		runOnUiThread(checkSerNameOkRunnable);
            	else
            		runOnUiThread(checkSerNameErrorRunnable);
            }
        }.start();
    }
	
    Runnable checkPhoneOkRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckPhone = false;
        	checkPhone.setVisibility(View.VISIBLE);
    		checkPhone.setImageResource(R.drawable.signup_ok);
        }
    };
    
    Runnable checkPhoneErrorRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckPhone = true;
        	checkPhone.setVisibility(View.VISIBLE);
        	checkPhone.setImageResource(R.drawable.signup_error);
        }
    };
    
    Runnable checkNameOkRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckName = false;
        	checkName.setVisibility(View.VISIBLE);
    		checkName.setImageResource(R.drawable.signup_ok);
        }
    };
    
    Runnable checkNameErrorRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckName = true;
        	checkName.setVisibility(View.VISIBLE);
        	checkName.setImageResource(R.drawable.signup_error);
        }
    };
    
    Runnable checkSerNameOkRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckSername = false;
        	checkSername.setVisibility(View.VISIBLE);
        	checkSername.setImageResource(R.drawable.signup_ok);
        }
    };
    
    Runnable checkSerNameErrorRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckSername = true;
        	checkSername.setVisibility(View.VISIBLE);
        	checkSername.setImageResource(R.drawable.signup_error);
        }
    };
    
    private boolean hasOnlyLetters(String str) {
    	return str.matches("^[a-zA-ZА-Яа-я_-]+$");
    }
}
