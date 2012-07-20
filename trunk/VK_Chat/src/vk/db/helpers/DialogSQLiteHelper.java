package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DialogSQLiteHelper extends SQLiteOpenHelper {
	
	private static DialogSQLiteHelper mInstance = null;
	private Context mCxt;
	
	//Fields
	public static final String TABLE_DIALOG = "t_dialog";
	public static final String COLUMN_MID = "mid";
	public static final String COLUMN_UID = "uid";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_TITILE = "title";
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_READ_STATE= "read_state";
	public static final String COLUMN_IS_OUT = "is_out";
	public static final String COLUMN_CHAT_ID = "chat_id";
	public static final String COLUMN_ATTACHMENTS = "attachments";
	public static final String COLUMN_FMESSAGES = "fwd_messages";
	
	private static final String DATABASE_NAME = "t_dialog.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_DIALOG + "(" + COLUMN_MID + " integer, " 
									  + COLUMN_UID + " integer, "	
									  + COLUMN_DATE + " integer, "
									  + COLUMN_TITILE + " text, "
									  + COLUMN_BODY + " text, "
									  + COLUMN_READ_STATE + " boolean, "
									  + COLUMN_IS_OUT + " text, "
									  + COLUMN_CHAT_ID + " integer,"
									  + COLUMN_ATTACHMENTS + " boolean, "
									  + COLUMN_FMESSAGES + " boolean);";
	
	public DialogSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mCxt = context;
	}
	
	public static DialogSQLiteHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DialogSQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIALOG);
		onCreate(db);		
	}
	
	public void drop(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIALOG);
	}

}
