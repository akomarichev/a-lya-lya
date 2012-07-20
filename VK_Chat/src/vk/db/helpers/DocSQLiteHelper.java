package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DocSQLiteHelper extends SQLiteOpenHelper {
	
	private static DocSQLiteHelper mInstance = null;
	
	//Fields
	public static final String TABLE_DOCS = "docs";
	public static final String COLUMN_MID = "mid";
	public static final String COLUMN_DID = "did";
	public static final String COLUMN_OWNER_ID = "owner_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_SIZE = "size";
	public static final String COLUMN_URL = "url";	
	public static final String COLUMN_EXT = "ext";
	
	private static final String DATABASE_NAME = "docs.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_DOCS + "(" + COLUMN_MID + " text, "
									  + COLUMN_DID + " text, "	
									  + COLUMN_OWNER_ID + " text, "	
									  + COLUMN_TITLE + " text, "
									  + COLUMN_SIZE + " text, "
									  + COLUMN_URL + " text, "
									  + COLUMN_EXT + " text);";
	
	public DocSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static DocSQLiteHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DocSQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCS);
		onCreate(db);		
	}

}
