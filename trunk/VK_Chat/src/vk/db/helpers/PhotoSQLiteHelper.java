package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhotoSQLiteHelper extends SQLiteOpenHelper {
	
	//Fields
	public static final String TABLE_PHOTO = "photo";
	public static final String COLUMN_MID = "mid";
	public static final String COLUMN_SRC = "src";
	public static final String COLUMN_SRC_XBIG = "src_xbig";
	public static final String COLUMN_PHOTOTEXT = "phototext";
	
	private static final String DATABASE_NAME = "photo.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_PHOTO + "(" + COLUMN_MID + " text, "
									  + COLUMN_SRC + " text, "	
									  + COLUMN_SRC_XBIG + " text, "
									  + COLUMN_PHOTOTEXT + " text);";
	
	public PhotoSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
		onCreate(db);		
	}

}
