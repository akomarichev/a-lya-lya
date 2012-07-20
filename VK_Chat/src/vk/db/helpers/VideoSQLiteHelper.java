package vk.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VideoSQLiteHelper extends SQLiteOpenHelper {
	
	private static VideoSQLiteHelper mInstance = null;
	
	//Fields
	public static final String TABLE_VIDEO = "video";
	public static final String COLUMN_MID = "mid";
	public static final String COLUMN_VID = "vid";
	public static final String COLUMN_OWNER_ID = "owner_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESC = "description";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_IMAGE = "image";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_PLAYER = "player";
	
	private static final String DATABASE_NAME = "video.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
				+ TABLE_VIDEO + "(" + COLUMN_MID + " text, "
									  + COLUMN_VID + " text, "
									  + COLUMN_OWNER_ID + " text, "
									  + COLUMN_TITLE + " text, "	
									  + COLUMN_DESC + " text, "
									  + COLUMN_DURATION + " integer, "
									  + COLUMN_LINK + " text, "
									  + COLUMN_IMAGE + " text, "
									  + COLUMN_DATE + " text, "
									  + COLUMN_PLAYER + " integer);";
	
	public VideoSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static VideoSQLiteHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new VideoSQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
		
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
		onCreate(db);		
	}

}
