package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatSQLiteHelper extends SQLiteOpenHelper {
	
	//Fields
	public static final String TABLE_CHAT = "chat";
	public static final String COLUMN_CHAT_ID = "chat_id";
	public static final String COLUMN_UID = "uid";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_ONLINE = "online";
	public static final String COLUMN_PHOTO_REC = "photo_rec";
	public static final String COLUMN_MOBILEPHONE = "mobile_phone";
	
	private static final String DATABASE_NAME = "chat.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_CHAT + "(" + COLUMN_CHAT_ID + " integer, " 
									  + COLUMN_UID + " integer, " 
									  + COLUMN_FIRST_NAME + " text, "	
									  + COLUMN_LAST_NAME + " text, "
									  + COLUMN_ONLINE + " boolean, "
									  + COLUMN_PHOTO_REC + " text, "
									  + COLUMN_MOBILEPHONE + " text);";
	
	public ChatSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
		onCreate(db);		
	}

}
