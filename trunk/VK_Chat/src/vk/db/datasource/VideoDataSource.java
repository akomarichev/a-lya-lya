package vk.db.datasource;

import java.util.ArrayList;

import vk.api.Video;
import vk.db.helpers.DialogSQLiteHelper;
import vk.db.helpers.DocSQLiteHelper;
import vk.db.helpers.VideoSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class VideoDataSource {
	private SQLiteDatabase database;
	private VideoSQLiteHelper dbHelper;
	
	private String[] allColumns = { 
			VideoSQLiteHelper.COLUMN_MID,
			VideoSQLiteHelper.COLUMN_VID,
			VideoSQLiteHelper.COLUMN_OWNER_ID,
			VideoSQLiteHelper.COLUMN_TITLE,
			VideoSQLiteHelper.COLUMN_DESC,
			VideoSQLiteHelper.COLUMN_DURATION,
			VideoSQLiteHelper.COLUMN_LINK,
			VideoSQLiteHelper.COLUMN_IMAGE,
			VideoSQLiteHelper.COLUMN_DATE,
			VideoSQLiteHelper.COLUMN_PLAYER
	};
	
	public VideoDataSource(Context context) {
		dbHelper = VideoSQLiteHelper.getInstance(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void deleteVideo(String mid) {
		database.delete(VideoSQLiteHelper.TABLE_VIDEO, VideoSQLiteHelper.COLUMN_MID
				+ " = " + mid, null);
	}
	
	public void addVideo(ArrayList<Video> videos, String mid){
		ContentValues values = new ContentValues();
		for(Video video:videos){
			values.put(VideoSQLiteHelper.COLUMN_MID, mid);
			values.put(VideoSQLiteHelper.COLUMN_VID, video.vid);
			values.put(VideoSQLiteHelper.COLUMN_OWNER_ID, video.owner_id);
			values.put(VideoSQLiteHelper.COLUMN_TITLE, video.title);
			values.put(VideoSQLiteHelper.COLUMN_DESC, video.description);
			values.put(VideoSQLiteHelper.COLUMN_DURATION, video.duration);
			values.put(VideoSQLiteHelper.COLUMN_LINK, video.link);
			values.put(VideoSQLiteHelper.COLUMN_IMAGE, video.image);
			values.put(VideoSQLiteHelper.COLUMN_DATE, video.date);
			values.put(VideoSQLiteHelper.COLUMN_PLAYER, video.player);
			database.insert(VideoSQLiteHelper.TABLE_VIDEO, null, values);			
		}
	}
	
	public ArrayList<Video> getAllVideos(String mid) {
		ArrayList<Video> videos = new ArrayList<Video>();
		
		String WHERE = VideoSQLiteHelper.COLUMN_MID + " = " + mid;

		Cursor cursor = database.query(VideoSQLiteHelper.TABLE_VIDEO,
				allColumns, WHERE, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Video video = cursorToVideo(cursor);
			videos.add(video);
			cursor.moveToNext();
		}
		cursor.close();
		return videos;
	}
	
	private Video cursorToVideo(Cursor cursor) {
		Video video = new Video();
		video.vid = Long.parseLong(cursor.getString(1));
		video.owner_id = Long.parseLong(cursor.getString(2));
		video.title = cursor.getString(3);
		video.description = cursor.getString(4);
		video.duration = cursor.getLong(5);
		video.link = cursor.getString(6);
		video.image = cursor.getString(7);
		video.date = cursor.getLong(8);
		video.player = cursor.getString(9);
		return video;
	}
	
	public void removeAll()
	{
	    database.delete(VideoSQLiteHelper.TABLE_VIDEO, null, null);
	}
}
