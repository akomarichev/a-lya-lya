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
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmActivity extends Activity {	
	private ImageView iv_back;
	private Typeface tf;
	private API api;
	
	private ImageView checkPhone;
	private ImageView checkCode;
	private ImageView checkPassword;
	private ImageView checkRePassword;
	
	private EditText et_checkPhone;
	private EditText et_checkCode;
	private EditText et_checkPassword;
	private EditText et_checkRePassword;
	
	private Button signup;
	private Button cancel;
	
	private Boolean errorCheckPhone = true;
	private Boolean errorCheckCode= true;
	private Boolean errorCheckPassword = true;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirm);
		
		api = new API(Constants.ACCESS_TOKEN);
		
		setupUI();
	}

	private void setupUI(){
		iv_back = (ImageView) findViewById(R.id.signup_back);
		iv_back.setOnClickListener(backClick);		
		
		checkPhone = (ImageView) findViewById(R.id.iv_signup_phone);
		checkPhone.setVisibility(View.GONE);
		checkCode = (ImageView) findViewById(R.id.iv_signup_code);
		checkCode.setVisibility(View.GONE);
		checkPassword = (ImageView) findViewById(R.id.iv_signup_pass);
		checkPassword.setVisibility(View.GONE);
		checkRePassword = (ImageView) findViewById(R.id.iv_signup_repass);
		checkRePassword.setVisibility(View.GONE);
		
		et_checkPhone = (EditText) findViewById(R.id.et_confirm_phone);
		et_checkPhone.addTextChangedListener(et_checkPhoneListener);
		et_checkCode = (EditText) findViewById(R.id.et_signup_code);
		et_checkCode.addTextChangedListener(et_checkCodeListener);
		et_checkPassword = (EditText) findViewById(R.id.et_signup_pass);
		et_checkPassword.addTextChangedListener(et_checkPasswordListener);
		et_checkRePassword = (EditText) findViewById(R.id.et_signup_repass);
		et_checkRePassword.addTextChangedListener(et_checkRePasswordListener);
		
		signup = (Button) findViewById(R.id.b_confirm);
		signup.setOnClickListener(signupClick);
		
		cancel = (Button) findViewById(R.id.b_cancel);
		cancel.setOnClickListener(cancelClick);
	}
	
	private void setupFonts(){
		tf = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");
		signup.setTypeface(tf);
		cancel.setTypeface(tf);
		et_checkPhone.setTypeface(tf);
		et_checkPassword.setTypeface(tf);
		et_checkRePassword.setTypeface(tf);
		et_checkCode.setTypeface(tf);
	}
	
	private OnClickListener backClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(ConfirmActivity.this, LoginActivity.class));
    		finish();				
		}
	};
	
	private OnClickListener signupClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			if(errorCheckPhone)
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_phone_error), Toast.LENGTH_LONG).show();
			else if(errorCheckCode)
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_code_error), Toast.LENGTH_LONG).show();
			else if(errorCheckPassword)
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_pass_error), Toast.LENGTH_LONG).show();			
			else{
				int result=0;
				
				try {
					result = api.confirmPhone(et_checkPhone.getText().toString(),
							et_checkCode.getText().toString(),
							et_checkPassword.getText().toString());
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
				if(result==1){
					Pref.cancelSignUp(ConfirmActivity.this);
		        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_confirm_ok), Toast.LENGTH_LONG).show();
					startActivity(new Intent(ConfirmActivity.this, LoginActivity.class));
					finish();
				} else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_confirm_error), Toast.LENGTH_LONG).show();
				}
			}
		}
	};
	
	private OnClickListener cancelClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			Pref.cancelSignUp(ConfirmActivity.this);
			startActivity(new Intent(ConfirmActivity.this, LoginActivity.class));
			finish();
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
	
	private TextWatcher et_checkCodeListener = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			checkCode(et_checkCode.getText().toString());
		}		
	};
	
	private TextWatcher et_checkPasswordListener = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			checkPassword(et_checkPassword.getText().toString());
		}		
	};
	
	private TextWatcher et_checkRePasswordListener = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			checkRePassword(et_checkRePassword.getText().toString());
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
    
    private void checkCode(final String Code) {
        new Thread(){
            @Override
            public void run(){
            	if(checkPass(Code))
            		runOnUiThread(checkCodeOkRunnable);
            	else
            		runOnUiThread(checkCodeErrorRunnable);
            }
        }.start();
    }
    
    private void checkPassword(final String Password) {
        new Thread(){
            @Override
            public void run(){
            	if(checkPass(Password) && Password.length() >= 6)
            		runOnUiThread(checkPasswordOkRunnable);
            	else
            		runOnUiThread(checkPasswordErrorRunnable);
            }
        }.start();
    }
    
    private void checkRePassword(final String RePassword) {
        new Thread(){
            @Override
            public void run(){
            	if(checkPass(RePassword) && RePassword.length() >= 6 && et_checkPassword.getText().toString().equals(RePassword))
            		runOnUiThread(checkRePasswordOkRunnable);
            	else
            		runOnUiThread(checkRePasswordErrorRunnable);
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
    
    Runnable checkCodeOkRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckCode = false;
        	checkCode.setVisibility(View.VISIBLE);
    		checkCode.setImageResource(R.drawable.signup_ok);
        }
    };
    
    Runnable checkCodeErrorRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckCode = true;
        	checkCode.setVisibility(View.VISIBLE);
        	checkCode.setImageResource(R.drawable.signup_error);
        }
    };
    
    Runnable checkPasswordOkRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckPassword = false;
        	checkPassword.setVisibility(View.VISIBLE);
        	checkPassword.setImageResource(R.drawable.signup_ok);
        }
    };
    
    Runnable checkPasswordErrorRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckPassword = true;
        	checkPassword.setVisibility(View.VISIBLE);
        	checkPassword.setImageResource(R.drawable.signup_error);
        }
    };
    
    Runnable checkRePasswordOkRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckPassword = false;
        	checkRePassword.setVisibility(View.VISIBLE);
        	checkRePassword.setImageResource(R.drawable.signup_ok);
        }
    };
    
    Runnable checkRePasswordErrorRunnable=new Runnable(){
        @Override
        public void run() {
        	errorCheckPassword = true;
        	checkRePassword.setVisibility(View.VISIBLE);
        	checkRePassword.setImageResource(R.drawable.signup_error);
        }
    };
    
    private boolean checkPass(String str) {
    	return str.matches("^[a-zA-Z0-9@\\$=!:.#%]+$");
    }
}
