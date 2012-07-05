package vk.db.datasource;

import java.util.ArrayList;

import vk.api.Attachment;
import vk.api.Message;
import vk.api.Photo;
import vk.api.User;
import vk.db.helpers.ConversationsSQLiteHelper;
import vk.db.helpers.DialogSQLiteHelper;
import vk.db.helpers.FriendsSQLiteHelper;
import vk.dialog.PhotoViewer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DialogDataSource {
	private SQLiteDatabase database;
	private DialogSQLiteHelper dbHelper;
	
	private PhotoDataSource db_photos;
	
	private String[] allColumns = { 
			DialogSQLiteHelper.COLUMN_MID,
			DialogSQLiteHelper.COLUMN_UID,
			DialogSQLiteHelper.COLUMN_DATE,
			DialogSQLiteHelper.COLUMN_TITILE,
			DialogSQLiteHelper.COLUMN_BODY,
			DialogSQLiteHelper.COLUMN_READ_STATE,
			DialogSQLiteHelper.COLUMN_IS_OUT,
			DialogSQLiteHelper.COLUMN_CHAT_ID,
			DialogSQLiteHelper.COLUMN_ATTACHMENTS,
			DialogSQLiteHelper.COLUMN_FMESSAGES
	};
	
	public DialogDataSource(Context context) {
		dbHelper = new DialogSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void drop() {
		dbHelper.drop(database);
	}
	
	public void deleteDialogWithUser(Message mess) {
		String uid = Long.toString(mess.uid);
		database.delete(DialogSQLiteHelper.TABLE_DIALOG, DialogSQLiteHelper.COLUMN_UID
				+ " = " + uid, null);
	}
	
	public void addMessageToDialog(ArrayList<Message> messages, Context context){
		ContentValues values = new ContentValues();
		for(Message message:messages){
			values.put(DialogSQLiteHelper.COLUMN_MID, message.mid);
			values.put(DialogSQLiteHelper.COLUMN_UID, message.uid);
			values.put(DialogSQLiteHelper.COLUMN_DATE, message.date);
			values.put(DialogSQLiteHelper.COLUMN_TITILE, message.title);
			values.put(DialogSQLiteHelper.COLUMN_BODY, message.body);
			values.put(DialogSQLiteHelper.COLUMN_READ_STATE, message.read_state);
			values.put(DialogSQLiteHelper.COLUMN_IS_OUT, message.is_out);
			values.put(DialogSQLiteHelper.COLUMN_CHAT_ID, message.chat_id);
			if(message.attachments != null && message.attachments.size() != 0){	
				Log.d("attachments",message.attachments.toString());
				ArrayList<Photo> photos = new ArrayList();
				for(Attachment att:message.attachments){
					if(att.type.equals("photo")){
						photos.add(att.photo);
					}
					Log.d("photo.size", Integer.toString(photos.size()));
				}
				if(photos.size()!=0){
					db_photos = new PhotoDataSource(context);
					db_photos.open();
					db_photos.deletePhotos(message.mid);
					db_photos.addPhoto(photos, message.mid);
					db_photos.close();
				}
				values.put(DialogSQLiteHelper.COLUMN_ATTACHMENTS, true);
			}
			else
				values.put(DialogSQLiteHelper.COLUMN_ATTACHMENTS, false);
			values.put(DialogSQLiteHelper.COLUMN_FMESSAGES, false);
			database.insert(DialogSQLiteHelper.TABLE_DIALOG, null, values);		
		}
	}
	
	public ArrayList<Message> getUserDialog(Long uid, Context context) {
		ArrayList<Message> dialog = new ArrayList<Message>();
		
		String WHERE = DialogSQLiteHelper.COLUMN_UID + " = " + Long.toString(uid);

		Cursor cursor = database.query(DialogSQLiteHelper.TABLE_DIALOG,
				allColumns, WHERE, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Message d = cursorToDialog(cursor, context);
			dialog.add(d);
			cursor.moveToNext();
		}
		cursor.close();
		return dialog;
	}
	
	private Message cursorToDialog(Cursor cursor, Context context) {
		Message d = new Message();
		Boolean atts;
		d.mid = cursor.getString(0);
		d.uid = cursor.getLong(1);
		d.date = cursor.getString(2);
		d.title = cursor.getString(3);
		d.body = cursor.getString(4);
		d.read_state = cursor.getString(5);
		d.is_out = (cursor.getInt(6) == 1);
		d.chat_id = cursor.getLong(7);
		atts = (cursor.getInt(8)==1);
		if(atts)
			d.attachments = getAttachments(d.mid, context);
		else
			d.attachments = null;
		d.f_msgs = null;
		return d;
	}
	
	public void removeAll()
	{
	    database.delete(DialogSQLiteHelper.TABLE_DIALOG, null, null);
	}
	
	public ArrayList<Attachment> getAttachments(String mid, Context context){
		ArrayList<Attachment> attachments = new ArrayList<Attachment>();
		ArrayList<Photo> photos = new ArrayList<Photo>();
		db_photos = new PhotoDataSource(context);
		db_photos.open();
		photos = db_photos.getAllPhotos(mid);
		db_photos.close();
		for(Photo photo:photos){
			Attachment attachment=new Attachment();
			attachment.type="photo";
			attachment.photo = photo;
			attachments.add(attachment);
		}
		Log.d("attachments.size()", Integer.toString(attachments.size()));
		return attachments;
	}
}
