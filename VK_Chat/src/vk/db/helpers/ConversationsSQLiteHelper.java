package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConversationsSQLiteHelper extends SQLiteOpenHelper {
	
	private static ConversationsSQLiteHelper mInstance = null;
	
	//Fields
	public static final String TABLE_CONVERSATIONS = "conversations";
	public static final String COLUMN_MID = "mid";
	public static final String COLUMN_UID = "uid";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_TITILE = "title";
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_READ_STATE= "read_state";
	public static final String COLUMN_IS_OUT = "is_out";
	public static final String COLUMN_CHAT_ID = "chat_id";
	
	private static final String DATABASE_NAME = "conversations.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_CONVERSATIONS + "(" + COLUMN_MID + " integer, " 
									  + COLUMN_UID + " integer, "	
									  + COLUMN_DATE + " integer, "
									  + COLUMN_TITILE + " text, "
									  + COLUMN_BODY + " text, "
									  + COLUMN_READ_STATE + " boolean, "
									  + COLUMN_IS_OUT + " text, "
									  + COLUMN_CHAT_ID + " integer);";
	
	public ConversationsSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static ConversationsSQLiteHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new ConversationsSQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATIONS);
		onCreate(db);		
	}

}
