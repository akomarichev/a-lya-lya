package vk.db.datasource;

import java.util.ArrayList;

import vk.api.Audio;
import vk.db.helpers.AudioSQLiteHelper;
import vk.db.helpers.DialogSQLiteHelper;
import vk.db.helpers.DocSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AudioDataSource {
	private SQLiteDatabase database;
	private AudioSQLiteHelper dbHelper;
	
	private String[] allColumns = { 
			AudioSQLiteHelper.COLUMN_MID,
			AudioSQLiteHelper.COLUMN_AID,
			AudioSQLiteHelper.COLUMN_ARTIST,
			AudioSQLiteHelper.COLUMN_TITLE,
			AudioSQLiteHelper.COLUMN_DURATION,
			AudioSQLiteHelper.COLUMN_URL
	};
	
	public AudioDataSource(Context context) {
		dbHelper = AudioSQLiteHelper.getInstance(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void deleteAudio(String mid) {
		database.delete(AudioSQLiteHelper.TABLE_AUDIO, AudioSQLiteHelper.COLUMN_MID
				+ " = " + mid, null);
	}
	
	public void addAudio(ArrayList<Audio> audios, String mid){
		ContentValues values = new ContentValues();
		for(Audio audio:audios){
			values.put(AudioSQLiteHelper.COLUMN_MID, mid);
			values.put(AudioSQLiteHelper.COLUMN_AID, audio.aid+"");
			values.put(AudioSQLiteHelper.COLUMN_ARTIST, audio.artist);
			values.put(AudioSQLiteHelper.COLUMN_TITLE, audio.title);
			values.put(AudioSQLiteHelper.COLUMN_DURATION, audio.duration);
			values.put(AudioSQLiteHelper.COLUMN_URL, audio.url);
			database.insert(AudioSQLiteHelper.TABLE_AUDIO, null, values);			
		}
	}
	
	public ArrayList<Audio> getAllAudios(String mid) {
		ArrayList<Audio> audios = new ArrayList<Audio>();
		
		String WHERE = AudioSQLiteHelper.COLUMN_MID + " = " + mid;

		Cursor cursor = database.query(AudioSQLiteHelper.TABLE_AUDIO,
				allColumns, WHERE, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Audio Audio = cursorToAudio(cursor);
			audios.add(Audio);
			cursor.moveToNext();
		}
		cursor.close();
		return audios;
	}
	
	private Audio cursorToAudio(Cursor cursor) {
		Audio audio = new Audio();
		audio.aid = Long.parseLong(cursor.getString(1));
		audio.artist = cursor.getString(2);
		audio.title = cursor.getString(3);
		audio.duration = cursor.getLong(4);
		audio.url = cursor.getString(5);
		return audio;
	}
	
	public void removeAll()
	{
	    database.delete(AudioSQLiteHelper.TABLE_AUDIO, null, null);
	}
}
