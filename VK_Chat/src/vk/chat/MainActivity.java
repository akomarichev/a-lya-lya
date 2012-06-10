//package vk.chat;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//public class MainActivity extends Activity {
//    
//    private final int REQUEST_LOGIN=1;
//    
//    Button authorizeButton;
//    Button logoutButton;
//    Button postButton;
//    Button friendsButton;
//    EditText messageEditText;
//    
//    //Account Account=new Account();
//    //Api api;
//    
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);       
//        //MessangerApplication appState = ((MessangerApplication)getApplicationContext());
//        
//        //�������������� ���������� ������
//        Account.restore(this);
//        //SharedPreferences email = getSharedPreferences("MYPREFS", 0);
//        
//        //Account.access_token = email.getString("email", "");
//        //Account.user_id = email.getLong(key, defValue)
//        
//        
//        //���� ������ ���� ������ API ��� ��������� � �������
//        if(Account.access_token!=null)
//            api=new Api(Account.access_token, Constants.API_ID);
//        
//        showButtons();
//    }
//    
//    @Override
//    protected void onStop() {
//    	super.onStop();
//    	
//    }
//    
//    
//    
//    private OnClickListener authorizeClick=new OnClickListener(){
//        @Override
//        public void onClick(View v) {
//            startLoginActivity();
//            
//        	//startRegistrationActivity();
//        }
//    };
//    
//    private OnClickListener logoutClick=new OnClickListener(){
//        @Override
//        public void onClick(View v) {
//            logOut();
//        }
//    };
//    
//    private OnClickListener postClick=new OnClickListener(){
//        @Override
//        public void onClick(View v) {
//        	postToWall();
//        }
//    };
//    
//    private OnClickListener friendsClick = new OnClickListener() {		
//		@Override
//		public void onClick(View v) {
//			//startFriendsActivity();	
//			startMessagesActivity();
//		}
//	};
//    
//	private void startFriendsActivity(){
//		Intent intent = new Intent();
//		intent.setClass(this, FriendsActivity.class);
//		//startActivity(intent);
//		startActivityForResult(intent, REQUEST_LOGIN);
//	}
//    
//	private void startMessagesActivity(){
//		Intent intent = new Intent();
//		intent.setClass(this, MessagesActivity.class);
//		//startActivity(intent);
//		startActivityForResult(intent, REQUEST_LOGIN);		
//	}
//	
//    private void startRegistrationActivity(){
//        Intent intent = new Intent();
//        intent.setClass(this, RegistrationActivity.class);
//        startActivityForResult(intent, REQUEST_LOGIN);    	
//    }
//    
//    private void startLoginActivity() {
//        Intent intent = new Intent();
//        intent.setClass(this, LoginActivity.class);
//        startActivityForResult(intent, REQUEST_LOGIN);
//    }
//    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_LOGIN) {
//            if (resultCode == RESULT_OK) {
//                Account.access_token=data.getStringExtra("token");
//                Account.user_id=data.getLongExtra("user_id", 0);
//                
//                Account.save(MainActivity.this);
//                api=new Api(Account.access_token, Constants.API_ID);
//                showButtons();
//            }
//        }
//    }
//    
//    private void getFriends(){
//    	new Thread(){
//    		public void run(){
//    			try{
//    				ArrayList<User> list = api.getFriends(Account.user_id, null, null);    				
//    				int x = 2;
//    			} catch(Exception e){    				
//    			}
//    		}
//    	}.start();
//    }
//    
//    Runnable successRunnable=new Runnable(){
//        @Override
//        public void run() {
//            Toast.makeText(getApplicationContext(), "Отправленно", Toast.LENGTH_LONG).show();
//        }
//    }; 
//}