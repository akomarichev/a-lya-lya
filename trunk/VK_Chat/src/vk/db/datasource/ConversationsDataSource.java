package vk.db.datasource;

import java.util.ArrayList;

import vk.api.Message;
import vk.api.User;
import vk.db.helpers.ConversationsSQLiteHelper;
import vk.db.helpers.FriendsSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ConversationsDataSource {
	private SQLiteDatabase database;
	private ConversationsSQLiteHelper dbHelper;
	
	private String[] allColumns = { 
			ConversationsSQLiteHelper.COLUMN_MID,
			ConversationsSQLiteHelper.COLUMN_UID,
			ConversationsSQLiteHelper.COLUMN_DATE,
			ConversationsSQLiteHelper.COLUMN_TITILE,
			ConversationsSQLiteHelper.COLUMN_BODY,
			ConversationsSQLiteHelper.COLUMN_READ_STATE,
			ConversationsSQLiteHelper.COLUMN_IS_OUT,
			ConversationsSQLiteHelper.COLUMN_CHAT_ID
	};
	
	public ConversationsDataSource(Context context) {
		dbHelper = new ConversationsSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void deleteConversation(Message conversation) {
		String id = conversation.mid;
		database.delete(ConversationsSQLiteHelper.TABLE_CONVERSATIONS, ConversationsSQLiteHelper.COLUMN_MID
				+ " = " + id, null);
	}
	
	public void addConversations(ArrayList<Message> conversations){
		ContentValues values = new ContentValues();
		for(Message conversation:conversations){
			values.put(ConversationsSQLiteHelper.COLUMN_MID, conversation.mid);
			values.put(ConversationsSQLiteHelper.COLUMN_UID, conversation.uid);
			values.put(ConversationsSQLiteHelper.COLUMN_DATE, conversation.date);
			values.put(ConversationsSQLiteHelper.COLUMN_TITILE, conversation.title);
			values.put(ConversationsSQLiteHelper.COLUMN_BODY, conversation.body);
			values.put(ConversationsSQLiteHelper.COLUMN_READ_STATE, conversation.read_state);
			values.put(ConversationsSQLiteHelper.COLUMN_IS_OUT, conversation.is_out);
			values.put(ConversationsSQLiteHelper.COLUMN_CHAT_ID, conversation.chat_id);
			database.insert(ConversationsSQLiteHelper.TABLE_CONVERSATIONS, null, values);			
		}
	}
	
	public ArrayList<Message> getAllConversations() {
		ArrayList<Message> conversations = new ArrayList<Message>();

		Cursor cursor = database.query(ConversationsSQLiteHelper.TABLE_CONVERSATIONS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Message conversation = cursorToConversation(cursor);
			conversations.add(conversation);
			cursor.moveToNext();
		}
		cursor.close();
		return conversations;
	}
	
	private Message cursorToConversation(Cursor cursor) {
		Message conversation = new Message();
		conversation.mid = cursor.getString(0);
		conversation.uid = cursor.getLong(1);
		conversation.date = cursor.getString(2);
		conversation.title = cursor.getString(3);
		conversation.body = cursor.getString(4);
		conversation.read_state = cursor.getString(5);
		conversation.is_out = (cursor.getInt(6) == 1);
		conversation.chat_id = cursor.getLong(7);
		return conversation;
	}
	
	public void removeAll()
	{
	    database.delete(ConversationsSQLiteHelper.TABLE_CONVERSATIONS, null, null);
	}
}
