package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendsSQLiteHelper extends SQLiteOpenHelper {
	
	private static FriendsSQLiteHelper mInstance = null;
	
	//Fields
	public static final String TABLE_FRIENDS = "friends";
	public static final String COLUMN_UID = "uid";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_ONLINE = "online";
	public static final String COLUMN_PHOTO_REC = "photo_rec";
	public static final String COLUMN_MOBILEPHONE = "mobile_phone";
	
	private static final String DATABASE_NAME = "friends.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_FRIENDS + "(" + COLUMN_UID + " integer, " 
									  + COLUMN_FIRST_NAME + " text, "	
									  + COLUMN_LAST_NAME + " text, "
									  + COLUMN_ONLINE + " boolean, "
									  + COLUMN_PHOTO_REC + " text, "
									  + COLUMN_MOBILEPHONE + " text);";
	
	public FriendsSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static FriendsSQLiteHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new FriendsSQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
		
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
		onCreate(db);		
	}

}
