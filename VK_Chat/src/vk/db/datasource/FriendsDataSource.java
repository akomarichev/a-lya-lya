package vk.db.datasource;

import java.util.ArrayList;

import vk.api.ForwardMessages;
import vk.api.User;
import vk.db.helpers.DialogSQLiteHelper;
import vk.db.helpers.FriendsSQLiteHelper;
import vk.db.helpers.FwdSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FriendsDataSource {
	private SQLiteDatabase database;
	private FriendsSQLiteHelper dbHelper;
	
	private String[] allColumns = { 
			FriendsSQLiteHelper.COLUMN_UID,
			FriendsSQLiteHelper.COLUMN_FIRST_NAME,
			FriendsSQLiteHelper.COLUMN_LAST_NAME,
			FriendsSQLiteHelper.COLUMN_ONLINE,
			FriendsSQLiteHelper.COLUMN_PHOTO_REC,
			FriendsSQLiteHelper.COLUMN_MOBILEPHONE
	};
	
	public FriendsDataSource(Context context) {
		dbHelper = FriendsSQLiteHelper.getInstance(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
		database.close();
	}
	
	public void deleteFriend(User friend) {
		long id = friend.uid;
		database.delete(FriendsSQLiteHelper.TABLE_FRIENDS, FriendsSQLiteHelper.COLUMN_UID
				+ " = " + id, null);
	}
	
	public void addFriends(ArrayList<User> friends){
		ContentValues values = new ContentValues();
		for(User friend:friends){
			values.put(FriendsSQLiteHelper.COLUMN_UID, friend.uid);
			values.put(FriendsSQLiteHelper.COLUMN_FIRST_NAME, friend.first_name);
			values.put(FriendsSQLiteHelper.COLUMN_LAST_NAME, friend.last_name);
			values.put(FriendsSQLiteHelper.COLUMN_ONLINE, friend.online);
			values.put(FriendsSQLiteHelper.COLUMN_PHOTO_REC, friend.photo_rec);
			values.put(FriendsSQLiteHelper.COLUMN_MOBILEPHONE, friend.mobile_phone);
			database.insert(FriendsSQLiteHelper.TABLE_FRIENDS, null, values);			
		}
	}
	
	public User getUserFromDB(Long uid){
		User user = new User();
		
		String WHERE = FwdSQLiteHelper.COLUMN_UID + " = " + uid;

		Cursor cursor = database.query(FriendsSQLiteHelper.TABLE_FRIENDS,
				allColumns, WHERE, null, null, null, null);
		if(cursor.moveToFirst()){
			user = cursorToFriend(cursor);
			cursor.close();
			return user;
		}
		
		return null;
	}
	
	public ArrayList<User> getAllFriends() {
		ArrayList<User> friends = new ArrayList<User>();

		Cursor cursor = database.query(FriendsSQLiteHelper.TABLE_FRIENDS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User friend = cursorToFriend(cursor);
			friends.add(friend);
			cursor.moveToNext();
		}
		cursor.close();
		return friends;
	}
	
	private User cursorToFriend(Cursor cursor) {
		User friend = new User();
		friend.uid = cursor.getLong(0);
		friend.first_name = cursor.getString(1);
		friend.last_name = cursor.getString(2);
		friend.online = (cursor.getInt(3) == 1);
		friend.photo_rec = cursor.getString(4);
		friend.mobile_phone = cursor.getString(5);
		return friend;
	}
	
	public void removeAll()
	{
	    database.delete(FriendsSQLiteHelper.TABLE_FRIENDS, null, null);
	}
}
