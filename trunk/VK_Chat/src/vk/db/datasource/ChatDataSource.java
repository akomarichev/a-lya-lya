package vk.db.datasource;

import java.util.ArrayList;

import vk.api.User;
import vk.db.helpers.ChatSQLiteHelper;
import vk.db.helpers.FriendsSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ChatDataSource {
	private SQLiteDatabase database;
	private ChatSQLiteHelper dbHelper;
	
	private String[] allColumns = { 
			ChatSQLiteHelper.COLUMN_CHAT_ID,
			ChatSQLiteHelper.COLUMN_UID,
			ChatSQLiteHelper.COLUMN_FIRST_NAME,
			ChatSQLiteHelper.COLUMN_LAST_NAME,
			ChatSQLiteHelper.COLUMN_ONLINE,
			ChatSQLiteHelper.COLUMN_PHOTO_REC,
			ChatSQLiteHelper.COLUMN_MOBILEPHONE
	};
	
	public ChatDataSource(Context context) {
		dbHelper = new ChatSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void deleteChatUser(User friend) {
		long id = friend.uid;
		database.delete(ChatSQLiteHelper.TABLE_CHAT, ChatSQLiteHelper.COLUMN_CHAT_ID
				+ " = " + id, null);
	}
	
	public void addChatUsers(ArrayList<User> chatUsers, Long chat_id){
		ContentValues values = new ContentValues();
		for(User chatUser:chatUsers){
			
			values.put(ChatSQLiteHelper.COLUMN_CHAT_ID, chat_id);
			values.put(ChatSQLiteHelper.COLUMN_UID, chatUser.uid);
			values.put(ChatSQLiteHelper.COLUMN_FIRST_NAME, chatUser.first_name);
			values.put(ChatSQLiteHelper.COLUMN_LAST_NAME, chatUser.last_name);
			values.put(ChatSQLiteHelper.COLUMN_ONLINE, chatUser.online);
			values.put(ChatSQLiteHelper.COLUMN_PHOTO_REC, chatUser.photo_rec);
			values.put(ChatSQLiteHelper.COLUMN_MOBILEPHONE, chatUser.mobile_phone);
			database.insert(ChatSQLiteHelper.TABLE_CHAT, null, values);			
		}
	}
	
	public ArrayList<User> getChatUsers(Long chat_id) {
		ArrayList<User> chatUsers = new ArrayList<User>();

		String sql = /*" SELECT * FROM " + ChatSQLiteHelper.TABLE_CHAT +"  WHERE " + */ChatSQLiteHelper.COLUMN_CHAT_ID + " = " + chat_id.toString();
		Cursor cursor = database.query(ChatSQLiteHelper.TABLE_CHAT,
				allColumns, sql, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User user = cursorToChatUser(cursor);
			Log.d("description", user.uid + " " + user.photo_rec);
			chatUsers.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return chatUsers;
	}
	
	private User cursorToChatUser(Cursor cursor) {
		User user = new User();
		user.uid = cursor.getLong(1);
		user.first_name = cursor.getString(2);
		user.last_name = cursor.getString(3);
		user.online = (cursor.getInt(4) == 1);
		user.photo_rec = cursor.getString(5);
		user.mobile_phone = cursor.getString(6);
		return user;
	}
	
	public Boolean isHasChat(Long chat_id){
		String sql = ChatSQLiteHelper.COLUMN_CHAT_ID + " = " + chat_id.toString()+";";
		Cursor cursor = database.query(ChatSQLiteHelper.TABLE_CHAT,
				allColumns, sql, null, null, null, null);
		if(cursor!=null && cursor.getCount()>0)
			return true;
		return false;
	}
	
	public void removeAll()
	{
	    database.delete(ChatSQLiteHelper.TABLE_CHAT, null, null);
	}
}
