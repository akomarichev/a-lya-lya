package vk.db.datasource;

import java.util.ArrayList;

import vk.api.Message;
import vk.api.Photo;
import vk.api.User;
import vk.db.helpers.ConversationsSQLiteHelper;
import vk.db.helpers.DialogSQLiteHelper;
import vk.db.helpers.FriendsSQLiteHelper;
import vk.db.helpers.PhotoSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PhotoDataSource {
	private SQLiteDatabase database;
	private PhotoSQLiteHelper dbHelper;
	
	
	private String[] allColumns = { 
			PhotoSQLiteHelper.COLUMN_MID,
			PhotoSQLiteHelper.COLUMN_SRC,
			PhotoSQLiteHelper.COLUMN_SRC_XBIG,
			PhotoSQLiteHelper.COLUMN_PHOTOTEXT
	};
	
	public PhotoDataSource(Context context) {
		dbHelper = new PhotoSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void deletePhotos(String mid) {
		database.delete(PhotoSQLiteHelper.TABLE_PHOTO, PhotoSQLiteHelper.COLUMN_MID
				+ " = " + mid, null);
	}
	
	public void addPhoto(ArrayList<Photo> photos, String mid){
		ContentValues values = new ContentValues();
		for(Photo photo:photos){
			Log.d("add_photo", "add_photo");
			values.put(PhotoSQLiteHelper.COLUMN_MID, mid);
			values.put(PhotoSQLiteHelper.COLUMN_SRC, photo.src);
			values.put(PhotoSQLiteHelper.COLUMN_SRC_XBIG, photo.src_xbig);
			values.put(PhotoSQLiteHelper.COLUMN_PHOTOTEXT, photo.phototext);
			database.insert(PhotoSQLiteHelper.TABLE_PHOTO, null, values);			
		}
	}
	
	public ArrayList<Photo> getAllPhotos(String mid) {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		String WHERE = PhotoSQLiteHelper.COLUMN_MID + " = " + mid;

		Cursor cursor = database.query(PhotoSQLiteHelper.TABLE_PHOTO,
				allColumns, WHERE, null, null, null, null);

		Log.d("cursor.size", cursor.toString());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo photo = cursorToPhoto(cursor);
			photos.add(photo);
			cursor.moveToNext();
		}
		cursor.close();
		return photos;
	}
	
	private Photo cursorToPhoto(Cursor cursor) {
		Photo photo = new Photo();
		photo.src = cursor.getString(1);
		photo.src_xbig = cursor.getString(2);
		photo.phototext = cursor.getString(3);
		return photo;
	}
	
	public void removeAll()
	{
	    database.delete(PhotoSQLiteHelper.TABLE_PHOTO, null, null);
	}
}
