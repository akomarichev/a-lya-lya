package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AudioSQLiteHelper extends SQLiteOpenHelper {
	
	//Fields
	public static final String TABLE_AUDIO = "audio";
	public static final String COLUMN_MID = "mid";
	public static final String COLUMN_ARTIST = "artist";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_URL = "url";
	
	private static final String DATABASE_NAME = "audio.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_AUDIO + "(" + COLUMN_MID + " text, "
									  + COLUMN_ARTIST + " text, "	
									  + COLUMN_TITLE + " text, "
									  + COLUMN_DURATION + " integer, "
									  + COLUMN_URL + " text);";
	
	public AudioSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIO);
		onCreate(db);		
	}

}
