package vk.db.datasource;

import java.util.ArrayList;

import vk.api.ForwardMessages;
import vk.db.helpers.DialogSQLiteHelper;
import vk.db.helpers.FwdSQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FwdDataSource {
	private SQLiteDatabase database;
	private FwdSQLiteHelper dbHelper;
	
	
	private String[] allColumns = { 
			FwdSQLiteHelper.COLUMN_MID,
			FwdSQLiteHelper.COLUMN_UID,
			FwdSQLiteHelper.COLUMN_DATE,
			FwdSQLiteHelper.COLUMN_TITLE,
			FwdSQLiteHelper.COLUMN_BODY
	};
	
	public FwdDataSource(Context context) {
		dbHelper = FwdSQLiteHelper.getInstance(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void deleteFwd(String mid) {
		database.delete(FwdSQLiteHelper.TABLE_FWD, FwdSQLiteHelper.COLUMN_MID
				+ " = " + mid, null);
	}
	
	public void addFwds(ArrayList<ForwardMessages> fwds, String mid){
		ContentValues values = new ContentValues();
		for(ForwardMessages fwd:fwds){
			Log.d("fwd", "fwd");
			values.put(FwdSQLiteHelper.COLUMN_MID, mid);
			values.put(FwdSQLiteHelper.COLUMN_UID, fwd.uid+"");
			values.put(FwdSQLiteHelper.COLUMN_DATE, fwd.date);
			values.put(FwdSQLiteHelper.COLUMN_TITLE, fwd.title);
			values.put(FwdSQLiteHelper.COLUMN_BODY, fwd.body);
			database.insert(FwdSQLiteHelper.TABLE_FWD, null, values);			
		}
	}
	
	public ArrayList<ForwardMessages> getAllFwds(String mid) {
		ArrayList<ForwardMessages> fwds = new ArrayList<ForwardMessages>();
		
		String WHERE = FwdSQLiteHelper.COLUMN_MID + " = " + mid;

		Cursor cursor = database.query(FwdSQLiteHelper.TABLE_FWD,
				allColumns, WHERE, null, null, null, null);

		//Log.d("cursor.size", cursor.toString());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ForwardMessages fwd = cursorToFwd(cursor);
			fwds.add(fwd);
			cursor.moveToNext();
		}
		cursor.close();
		return fwds;
	}
	
	private ForwardMessages cursorToFwd(Cursor cursor) {
		ForwardMessages fwd = new ForwardMessages();
		fwd.uid = Long.parseLong(cursor.getString(1));
		fwd.date = cursor.getString(2);
		fwd.title = cursor.getString(3);
		fwd.body = cursor.getString(4);
		Log.d("body", fwd.body);
		return fwd;
	}
	
	public void removeAll()
	{
	    database.delete(FwdSQLiteHelper.TABLE_FWD, null, null);
	}
}
