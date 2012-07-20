package vk.chat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import vk.adapters.ChatAdapter;
import vk.adapters.ConversationsAdapter;
import vk.adapters.DialogAdapter;
import vk.api.API;
import vk.api.Message;
import vk.api.User;
import vk.constants.Constants;
import vk.db.datasource.DialogDataSource;
import vk.db.datasource.FriendsDataSource;
import vk.horizontal.listview.ui.HorizontalListView;
import vk.popup.ActionItem;
import vk.popup.QuickAction;
import vk.pref.Pref;
import vk.utils.CheckConnection;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChatActivity extends Activity {
	
	private final static int TAKE_PICTURE = 1;
	private Uri mImageCaptureUri;	
	
	private AnimationDrawable loader;
	private String forwarded_messages = null;

	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	private static final int PICK_LOCATION = 3;
	
	private static final int ID_LOCATION = 1;
	private static final int ID_CAMERA   = 2;
	private static final int ID_GALLERY = 3;
	
	private static String photo_atts = "";
	
	private int max_attaches = 3;
	private int delete_position = -1;
	
	private API api;
	private Message m[];
	private ListView listView;
	
	private Boolean footerAdded = false;
	
	private long user_id;
	private long chat_id;
	private String type;
	private String title;
	private ArrayList<Integer> checkedItems = new ArrayList();
	//private ArrayList<User> chat_users =new ArrayList();
	public static ArrayList<User> chat_users = new ArrayList();
	private HorizontalListView  lv_horizontal;
	
	private MyListAdapter myListAdapter;
	private ArrayList<String> photo_paths;
	private LinearLayout dialog_linear_layout;
	private RelativeLayout header;
	public static LayoutInflater dialog_inflater;
	private Boolean header_bottons = false;
	
	private ImageView conv_attach;
	
	private ChatAdapter adapter;
	
	private Button cancel;
	private Button forward;
	private Button delete;
	
	private Button send;
	private EditText message;
	
	private View rowViewHeaderButton;
	
	private Button multichat;
	
	private ImageView back;
	private TextView title_tv;
	
	
	private ArrayList<Message> dialog;
	private ArrayList<Message> dialog_from_db;
	private ArrayList<Message> dialog_from_inet;
	//private DialogDataSource db_dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat);
        
        //create db
        //db_dialog = new DialogDataSource(this);
        
        
        conv_attach = (ImageView) findViewById(R.id.conv_attach);
        conv_attach.setClickable(true);
        send = (Button) findViewById(R.id.b_send_message);
        send.setOnClickListener(clickSend);
        
        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(backClick);
        
        title_tv = (TextView) findViewById(R.id.tv_title);
        
        message = (EditText) findViewById(R.id.dialog_et_message);
        
        multichat = (Button) findViewById(R.id.b_chatUsers);
        multichat.setOnClickListener(multichatClick);
        dialog_linear_layout = (LinearLayout) findViewById(R.id.dialog_linear_layout);
        dialog_inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowViewHeaderButton = dialog_inflater.inflate(R.layout.header_bottons, null, false);
        header = (RelativeLayout) findViewById(R.id.header_rel);
        
        setupUI();
        
        
        lv_horizontal  = (HorizontalListView) findViewById(R.id.lv_horizontal);
        photo_paths = new ArrayList();
        
        //LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //lv_horizontal.setLayoutParams(lp);
        //lv_horizontal.setVisibility(View.INVISIBLE);
        
        lv_horizontal.setVisibility(View.GONE);
        
        ActionItem nextItem 	= new ActionItem(ID_CAMERA, getResources().getString(R.string.vd_take_photo), getResources().getDrawable(R.drawable.attach_photo_lable));
		ActionItem prevItem 	= new ActionItem(ID_GALLERY, getResources().getString(R.string.vd_choose_exist_photo), getResources().getDrawable(R.drawable.attach_gallery_lable));
		ActionItem infoItem 	= new ActionItem(ID_LOCATION, getResources().getString(R.string.vd_share_location), getResources().getDrawable(R.drawable.attach_location_lable));
        
		
		//prevItem.setSticky(true);
        //nextItem.setSticky(true);
        //infoItem.setSticky(true);
        
        final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
		quickAction.addActionItem(infoItem);
		
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);
				
				
                
		        //photo_paths.add("date0");
		        //items.add("date1");
		        //items.add("date2");
		        //items.add("date3");
				//
		        
		        
		        conv_attach.setBackgroundResource(R.drawable.attach);
		        //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
		        //lv_horizontal.startAnimation(hyperspaceJumpAnimation);
		        
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_CAMERA) {
					takePhoto();
					//Toast.makeText(getApplicationContext(), "Take a photo!", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_GALLERY) {
					showGallery();
					//updateHorizontalListView();
					//Toast.makeText(getApplicationContext(), "Choose photo from gallery!", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_LOCATION) {
					getLocation();
				}
				
				
			}
		});
		
		conv_attach.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				conv_attach.setBackgroundResource(R.drawable.attach_pressed);
				quickAction.show(v);				
			}
		});
		
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
			@Override
			public void onDismiss() {
				conv_attach.setBackgroundResource(R.drawable.attach);
			}
		});
		
		Log.d("DialogActivity", "2");
        
		user_id = chat_id = 0;
		api = new API(Pref.getAccessTokenHTTPS(ChatActivity.this));
		
		type = getIntent().getExtras().getString("type");
		if(type.equals("uid")){
			user_id = getIntent().getExtras().getLong("uid");
			Intent i = getIntent();
			if(i.hasExtra("f_msgs")){
				forwarded_messages = getIntent().getExtras().getString("f_msgs");
				Toast.makeText(getApplicationContext(), forwarded_messages.toString(), Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getApplicationContext(), "not forwarded messages!", Toast.LENGTH_LONG).show();
		}
		else{
			if(getIntent().hasExtra("chat_id"))
				chat_id = getIntent().getExtras().getLong("chat_id");
			if(getIntent().hasExtra("title"))
				title = getIntent().getExtras().getString("title");
			try {
				if(CheckConnection.isOnline(this))
					chat_users = api.getChatUsers(chat_id);
				multichat.setText(chat_users.size()-1 + "  ");
				title_tv.setText(title);
				Log.d("chat_users",chat_users.toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
        
		listView = (ListView)findViewById(R.id.list_dialog);
        getDialog();   
	}
	
	private class MyListAdapter extends BaseAdapter {  
		
		private Context context;
	    private ArrayList<String> items;
	    
	    public MyListAdapter(Context context, ArrayList<String> items) {
	        this.context = context;
	        this.items = items;
	    }
		  
        @Override  
        public int getCount() {
            return items.size()+3;
        } 
  
        @Override  
        public String getItem(int position) {  
        	return items.get(position); 
        }  
  
        @Override  
        public long getItemId(int position) {  
        	return position; 
        }  
  
        @Override  
        public View getView(final int position, View convertView, ViewGroup parent) {  
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);    
            ImageView title = (ImageView) retval.findViewById(R.id.image);
            ImageView delete = (ImageView) retval.findViewById(R.id.iv_delete_button);
            delete.setVisibility(View.GONE);
            if(position==getCount()-1){
            	title.setBackgroundResource(R.drawable.horizontal_gallery);
            	title.setOnClickListener(clickGallery);
            	return retval;
            }
            if(position==getCount()-2){
            	title.setBackgroundResource(R.drawable.horizontal_geo);
            	title.setOnClickListener(clickGeo);
            	return retval;
            }
            if(position==getCount()-3){
            	title.setBackgroundResource(R.drawable.horizontal_photo);
            	title.setOnClickListener(clickPhoto);
            	return retval;
            }
            /*if(position==getCount()-5){
            	title.setBackgroundResource(R.drawable.icon);
            	delete.setVisibility(View.VISIBLE);
            	delete.setOnClickListener(new MyClickListener(position));*/
                /*delete.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                    		items.remove(position-3);
                    		notifyDataSetChanged();
                    }
                }
                );
            	return retval;
            }*/
            Bitmap bitmap 	= BitmapFactory.decodeFile(getItem(position));
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new MyClickListener(position));
            /*delete.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                		items.remove(position-3);
                		notifyDataSetChanged();
                }
            }
            );*/
            title.setBackgroundDrawable(new BitmapDrawable(bitmap));
            return retval;
        }   
        
        private class MyClickListener implements OnClickListener {

            private int position;

            public MyClickListener(int position) {
               this.position = position;
            }

            public void onClick(View v) {
            	Toast.makeText(getApplicationContext(), "position: " + position + ", image = " + items.get(position), Toast.LENGTH_SHORT).show();
            	items.remove(position);
            	if(items.size()==0){
            		lv_horizontal.setVisibility(View.GONE);
            		conv_attach.setBackgroundResource(R.drawable.attach);
            		conv_attach.setClickable(true);
            	}
        		notifyDataSetChanged();
            }

            public int getPosition() {
              return position;
            }

         }
    }; 
    
    public void setupUI(){
    	cancel = (Button) rowViewHeaderButton.findViewById(R.id.header_b_cancel);
    	cancel.setOnClickListener(cancelClick);
    	forward = (Button) rowViewHeaderButton.findViewById(R.id.header_b_forward);
    	forward.setOnClickListener(forwardClick);
    	delete = (Button) rowViewHeaderButton.findViewById(R.id.header_b_delete);
    	delete.setOnClickListener(deleteClick);
    }
    
    private OnClickListener cancelClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			checkedItems.clear();
			adapter.notifyDataSetChanged();
			header.setVisibility(View.VISIBLE);
			dialog_linear_layout.removeView(rowViewHeaderButton);
			header_bottons = false;
		}
	};
	
	private OnClickListener forwardClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(ChatActivity.this, FriendsActivity.class);
			String mids = "";
			for(Integer item:checkedItems)                		
        		mids += m[item].mid + ",";     
        	mids = mids.substring(0, mids.length() - 1);
			i.putExtra("f_msgs", mids);
			startActivity(i);
		}
	};
	
	private OnClickListener deleteClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			deleteMessages();
		}
	};
	
	private void deleteMessages() {
        new Thread(){
            @Override
            public void run(){
            	String response = null;
            	String mids="";
                try {        
                	for(Integer item:checkedItems)                		
                		mids += m[item].mid + ",";     
                	mids = mids.substring(0, mids.length() - 1);
                	Log.d("mids", mids);
                	response = api.deleteMessage(mids);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(response != null){                	             	
                	runOnUiThread(successDeleted);
                }
                else
                	runOnUiThread(notSuccessDeleted);
            }
        }.start();
    }
	
	Runnable successDeleted=new Runnable(){
        @Override
        public void run() {
        	checkedItems.clear();
			header.setVisibility(View.VISIBLE);
			dialog_linear_layout.removeView(rowViewHeaderButton);
			header_bottons = false;
			updateDialog();
        	//getDialog();
        	adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_success_deleted), Toast.LENGTH_LONG).show();
        }
    };
    
	Runnable notSuccessDeleted=new Runnable(){
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_message_not_delete) + " " + getResources().getString(R.string.vd_try_again), Toast.LENGTH_LONG).show();
        }
    };
	
	private OnClickListener clickSend=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			try{
				conv_attach.setBackgroundResource(R.anim.loader_grey);
				loader = (AnimationDrawable) conv_attach.getBackground();
				conv_attach.setClickable(false);
				loader.start();
				sendMessage(message.getText().toString(), null);
			}catch(Exception e){
				e.printStackTrace();				
			}
		}
	};
	
	private void sendMessage(final String message, final String title) {
        new Thread(){
            @Override
            public void run(){
            	String response=null;
            	if(photo_paths.size() != 0){
            		String res_upload [];
            		for(String photo_path:photo_paths){
            			try{
		            		String url = api.photosGetMessagesUploadServer();
		        			res_upload = api.uploadPhotoServer(url, photo_path);
		        			photo_atts += api.saveMessagesPhoto(res_upload[0],res_upload[1],res_upload[2]) + ",";
            			} catch(Exception e){
            				e.printStackTrace();
            			}
            		}
        			Log.d("photo_atts", photo_atts);
        			if(photo_atts != null){
        				photo_atts = photo_atts.substring(0, photo_atts.length() - 1);
        			}
            	}
                try {                	
                	if(chat_id != 0)
                		response = api.sendMessage(0L, chat_id, message, title, "1", photo_atts, null);
                	else
                		response = api.sendMessage(user_id, -1, message, title, "0", photo_atts, forwarded_messages);                   
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(response != null){                	             	
                	runOnUiThread(successSent);
                }
                else
                	runOnUiThread(notSuccessSent);
            }
        }.start();
    }
	
	Runnable successSent=new Runnable(){
        @Override
        public void run() {
        	loader.stop();
        	conv_attach.setBackgroundResource(R.drawable.attach);
        	conv_attach.setClickable(true);
        	updateDialog();
        	//getDialog();
        	adapter.notifyDataSetChanged();
        	photo_atts = "";
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_success_sent), Toast.LENGTH_LONG).show();
        }
    };
    
	Runnable notSuccessSent=new Runnable(){
        @Override
        public void run() {
        	loader.stop();
        	conv_attach.setBackgroundResource(R.drawable.attach);
        	conv_attach.setClickable(true);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.vd_message_not_send) + " " + getResources().getString(R.string.vd_try_again), Toast.LENGTH_LONG).show();
        }
    };
    
    private void getDialog(){    	
    	//db_dialog.open();
//    	
//    	Log.d("db_inserts", "db_inserts");
//    	
//    	//Pref.cancelLoadedDialogDB(DialogActivity.this);
//    	
//    	if(!Pref.loadedDialogDB(DialogActivity.this)){
    	dialog_from_db = VK_ChatActivity.db_dialog.getChatDialog(chat_id, this);
		//db_dialog.close();
		updateDialog();
    	//db_dialog.removeAll();
    	//loadDialogDb(dialog);
    //}
//    else{
//    	dialog = db_dialog.getUserDialog(user_id, DialogActivity.this);
//    	db_dialog.close();
//    }
		dialog = dialog_from_db;
//        	db_dialog.removeAll();
//        	loadDialogDb(dialog);
//        }
//        else{
//        	dialog = db_dialog.getUserDialog(user_id, DialogActivity.this);
//        	db_dialog.close();
//        }
        
    	Log.d("CHAT",Integer.toString(dialog.size()));
        m = new Message[dialog.size()];
		for(int i = 0; i<dialog.size(); i++ )
			m[dialog.size()-i-1]=dialog.get(i);
		
		adapter = new ChatAdapter(this, m, checkedItems);
		if(!footerAdded){
			View footerView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listitem_dialog_footer, null, false);
			listView.addFooterView(footerView);
			footerAdded = true;
		}
		listView.setAdapter(adapter);
		listView.setSelection(dialog.size()-1);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        
		        
		        
				//Log.d("dialog_activity","pressed");
				if(checkedItems.contains(position)){
					checkedItems.remove(checkedItems.indexOf(position));
					if(checkedItems.size()==0){
						header.setVisibility(View.VISIBLE);
						dialog_linear_layout.removeView(rowViewHeaderButton);
						header_bottons = false;
					}
				}
				else{
					checkedItems.add(position);	
					if(!header_bottons){
						header.setVisibility(View.GONE);
						dialog_linear_layout.addView(rowViewHeaderButton, 0);
						header_bottons = true;
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
    }
    
    private void updateDialog(){
   	 new Thread(){
            @Override
            public void run(){
           	 if(CheckConnection.isOnline(ChatActivity.this)){
           		//db_dialog.open();
            	dialog_from_db = VK_ChatActivity.db_dialog.getChatDialog(chat_id, ChatActivity.this);
        		//db_dialog.close();
	            	 try{
	                 	if(chat_id != 0)
	                 		dialog_from_inet = api.getMessagesHistory(0, chat_id, (long) 0, 100);
	                 	else
	                 		dialog_from_inet = api.getMessagesHistory(user_id, -20, (long) 0, 100);		
	         		} catch (Exception e){
	         			e.printStackTrace();
	         		} 
	            	 Log.d("ChatActivity update", "update");
	            	if(dialog_from_db != dialog_from_inet);
	            		runOnUiThread(needUpdateDialog);
           	 }
            }
        }.start();
   }
    
    Runnable needUpdateDialog=new Runnable(){
        @Override
        public void run() {
        	dialog = dialog_from_inet;
        	loadDialogDb(dialog_from_inet);
        	
        	m = new Message[dialog.size()];
    		for(int i = 0; i<dialog.size(); i++ )
    			m[dialog.size()-i-1]=dialog.get(i);
    		
    		adapter = new ChatAdapter(ChatActivity.this, m, checkedItems);
    		adapter.notifyDataSetChanged();
    		listView.setAdapter(adapter);
    		listView.setSelection(dialog.size()-1);	
        }
    };
    
    private void loadDialogDb(final ArrayList<Message> dialog) {
        new Thread(){
            @Override
            public void run(){
            	//db_dialog.open();
            	VK_ChatActivity.db_dialog.deleteChat(chat_id);
            	VK_ChatActivity.db_dialog.addMessageToDialog(dialog, ChatActivity.this, chat_id+"");
            	//Pref.setLoadedDialogDB(ChatActivity.this);
            	//db_dialog.close();
            	Log.d("db_inserted ChatActivity", "db_inserted");
            }
        }.start();
    }
    
    private void takePhoto(){
    	Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	File file		 = new File(Environment.getExternalStorageDirectory(),
	   			"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
    	mImageCaptureUri = Uri.fromFile(file);
    	try {			
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);

			startActivityForResult(intent, PICK_FROM_CAMERA);
		} catch (Exception e) {
			e.printStackTrace();
		}	
    }
    
    private void showGallery(){
    	Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;

		Bitmap bitmap 	= null;
		String path		= "";

		if (requestCode == PICK_FROM_FILE) {
			mImageCaptureUri = data.getData(); 
			path = getRealPathFromURI(mImageCaptureUri); //from Gallery 

			if (path == null)
				path = mImageCaptureUri.getPath(); //from File Manager

			if (path != null) 
				bitmap 	= BitmapFactory.decodeFile(path);
		} else if (requestCode == PICK_LOCATION){ 
			path = data.getStringExtra("path");
			bitmap 	= BitmapFactory.decodeFile(path);
    	}else {
			path	= mImageCaptureUri.getPath();
			bitmap  = BitmapFactory.decodeFile(path);
		}
		
		photo_paths.add(0, path);
		updateHorizontalListView();
		conv_attach.setClickable(false);
		//Log.d("path", path);
		
		//Toast.makeText(getApplicationContext(),
		//"path: " + path, Toast.LENGTH_LONG)
		//.show();
	}

	public String getRealPathFromURI(Uri contentUri) {
        String [] proj 		= {MediaStore.Images.Media.DATA};
        Cursor cursor 		= managedQuery( contentUri, proj, null, null,null);
        
        if (cursor == null) return null;
        
        int column_index 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}
	
	private void updateHorizontalListView(){
		myListAdapter = new MyListAdapter(this, photo_paths);     
        lv_horizontal.setAdapter(myListAdapter);
        lv_horizontal.setVisibility(View.VISIBLE);
	}
	
	private void takePhotoThread() {
        new Thread(){
            @Override
            public void run(){
            	takePhoto();
            	runOnUiThread(successTakePhoto);
            }
        }.start();
    }
	
	Runnable successTakePhoto=new Runnable(){
        @Override
        public void run() {
        	updateHorizontalListView();
        }
    };
    
    private OnClickListener multichatClick=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ChatActivity.this, ChatListActivity.class);
			intent.putExtra("chat_id", chat_id);
			intent.putExtra("title", title);
			startActivity(intent);
		}
    };
    
    private OnClickListener clickGallery=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			showGallery();
		}
	};
	
	private OnClickListener clickPhoto=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			takePhoto();
		}
	};
	
	private OnClickListener clickGeo=new View.OnClickListener(){		
		@Override
		public void onClick(View v) {
			getLocation();
		}
	};
	
	private void getLocation(){
		Intent intent = new Intent(ChatActivity.this, GMapsActivity.class);
        startActivityForResult(intent, PICK_LOCATION);
	}
	
	private OnClickListener backClick=new View.OnClickListener(){		
			@Override
			public void onClick(View v) {
				finish();
			}
		};
		
}
