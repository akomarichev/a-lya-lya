package vk.chat;

import java.util.ArrayList;

import vk.adapters.ConversationsAdapter;
import vk.adapters.DialogAdapter;
import vk.api.API;
import vk.api.Message;
import vk.popup.ActionItem;
import vk.popup.QuickAction;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DialogActivity extends Activity {
	
	private static final int ID_UP     = 1;
	private static final int ID_DOWN   = 2;
	private static final int ID_SEARCH = 3;
	private static final int ID_INFO   = 4;
	private static final int ID_ERASE  = 5;	
	private static final int ID_OK     = 6;
	

	private long user_id;
	private ArrayList<Integer> checkedItems = new ArrayList();
	
	
	private ArrayList<Message> dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        
        ImageView conv_attach = (ImageView) findViewById(R.id.conv_attach);
        
        ActionItem nextItem 	= new ActionItem(ID_DOWN, "Nextjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", getResources().getDrawable(R.drawable.menu_down_arrow));
		ActionItem prevItem 	= new ActionItem(ID_UP, "Prev", getResources().getDrawable(R.drawable.menu_up_arrow));
        
		
		prevItem.setSticky(true);
        nextItem.setSticky(true);
        
        final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
		
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);
                 
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_SEARCH) {
					Toast.makeText(getApplicationContext(), "Let's do some search action", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_INFO) {
					Toast.makeText(getApplicationContext(), "I have no info this time", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
			@Override
			public void onDismiss() {
				Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
			}
		});
		
		conv_attach.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});
        
        user_id = getIntent().getExtras().getLong("uid");
        
        API api = new API();
        
        try{
			dialog = api.getMessagesHistory(user_id, -20, (long) 0, 100);		
		} catch (Exception e){
			e.printStackTrace();
		} 
        
        Message m[] = new Message[dialog.size()];
		for(int i = 0; i<dialog.size(); i++ )
			m[dialog.size()-i-1]=dialog.get(i);	
        
		ListView listView = (ListView)findViewById(R.id.list_dialog);
        final DialogAdapter adapter = new DialogAdapter(this, m, checkedItems);
		listView.setAdapter(adapter);
		listView.setSelection(dialog.size()-1);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				checkedItems.add(position);
				adapter.notifyDataSetChanged();
			}
		});
	}

}
