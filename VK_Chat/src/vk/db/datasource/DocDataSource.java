package vk.db.datasource;

import java.util.ArrayList;

import vk.api.Doc;
import vk.db.helpers.DialogSQLiteHelper;
import vk.db.helpers.DocSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DocDataSource {
	private SQLiteDatabase database;
	private DocSQLiteHelper dbHelper;
	
	
	private String[] allColumns = { 
			DocSQLiteHelper.COLUMN_MID,
			DocSQLiteHelper.COLUMN_DID,
			DocSQLiteHelper.COLUMN_OWNER_ID,
			DocSQLiteHelper.COLUMN_TITLE,
			DocSQLiteHelper.COLUMN_SIZE,
			DocSQLiteHelper.COLUMN_URL,
			DocSQLiteHelper.COLUMN_EXT
	};
	
	public DocDataSource(Context context) {
		dbHelper = DocSQLiteHelper.getInstance(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void deleteDocs(String mid) {
		database.delete(DocSQLiteHelper.TABLE_DOCS, DocSQLiteHelper.COLUMN_MID
				+ " = " + mid, null);
	}
	
	public void addDocs(ArrayList<Doc> docs, String mid){
		ContentValues values = new ContentValues();
		for(Doc doc:docs){
			Log.d("add_docs", "add_docs");
			values.put(DocSQLiteHelper.COLUMN_MID, mid);
			values.put(DocSQLiteHelper.COLUMN_DID, doc.did+"");
			values.put(DocSQLiteHelper.COLUMN_OWNER_ID, doc.owner_id+"");
			values.put(DocSQLiteHelper.COLUMN_TITLE, doc.title);
			values.put(DocSQLiteHelper.COLUMN_SIZE, doc.size+"");
			values.put(DocSQLiteHelper.COLUMN_URL, doc.url);
			values.put(DocSQLiteHelper.COLUMN_EXT, doc.ext);
			database.insert(DocSQLiteHelper.TABLE_DOCS, null, values);			
		}
	}
	
	public ArrayList<Doc> getAllDocs(String mid) {
		ArrayList<Doc> docs = new ArrayList<Doc>();
		
		String WHERE = DocSQLiteHelper.COLUMN_MID + " = " + mid;

		Cursor cursor = database.query(DocSQLiteHelper.TABLE_DOCS,
				allColumns, WHERE, null, null, null, null);

		//Log.d("cursor.size", cursor.toString());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Doc doc = cursorToDoc(cursor);
			docs.add(doc);
			cursor.moveToNext();
		}
		cursor.close();
		return docs;
	}
	
	private Doc cursorToDoc(Cursor cursor) {
		Doc doc = new Doc();
		doc.did = Long.parseLong(cursor.getString(1));
		doc.owner_id = Long.parseLong(cursor.getString(2));
		doc.title = cursor.getString(3);
		doc.size =Long.parseLong(cursor.getString(4));
		doc.url = cursor.getString(5);
		doc.ext = cursor.getString(6);
		return doc;
	}
	
	public void removeAll()
	{
	    database.delete(DocSQLiteHelper.TABLE_DOCS, null, null);
	}
}
