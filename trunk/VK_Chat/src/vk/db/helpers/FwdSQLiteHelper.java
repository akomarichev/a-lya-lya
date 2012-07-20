package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FwdSQLiteHelper extends SQLiteOpenHelper {
	private static FwdSQLiteHelper mInstance = null;
	
	//Fields
	public static final String TABLE_FWD = "fwd";
	public static final String COLUMN_MID = "mid";
	public static final String COLUMN_UID = "uid";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_BODY = "body";
	
	private static final String DATABASE_NAME = "fwd.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_FWD + "(" + COLUMN_MID + " text, "
									  + COLUMN_UID + " text, "	
									  + COLUMN_DATE + " text, "
									  + COLUMN_TITLE + " text, "
									  + COLUMN_BODY + " text);";
	
	public FwdSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static FwdSQLiteHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new FwdSQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FWD);
		onCreate(db);		
	}

}
