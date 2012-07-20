package vk.db.datasource;

import java.util.ArrayList;

import vk.api.Attachment;
import vk.api.Audio;
import vk.api.Doc;
import vk.api.ForwardMessages;
import vk.api.Message;
import vk.api.Photo;
import vk.api.User;
import vk.api.Video;
import vk.db.helpers.ConversationsSQLiteHelper;
import vk.db.helpers.DialogSQLiteHelper;
import vk.db.helpers.FriendsSQLiteHelper;
import vk.db.helpers.PhotoSQLiteHelper;
import vk.dialog.AudioViewer;
import vk.dialog.DocViewer;
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
	
	private static PhotoDataSource db_photos;
	private static VideoDataSource db_videos;
	private static AudioDataSource db_audios;
	private static DocDataSource db_docs;
	private static FwdDataSource db_fwds;
	
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
		dbHelper = DialogSQLiteHelper.getInstance(context);
		db_photos = new PhotoDataSource(context);
		db_photos.open();
		db_videos = new VideoDataSource(context);
		db_videos.open();
		db_audios = new AudioDataSource(context);
		db_audios.open();
		db_docs = new DocDataSource(context);
		db_docs.open();
		db_fwds = new FwdDataSource(context);
		db_fwds.open();
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		db_photos.close();
		db_videos.close();
		db_audios.close();
		db_docs.close();
		db_fwds.close();
		dbHelper.close();
	}
	
	public void drop() {
		dbHelper.drop(database);
	}
	
	public void deleteDialogWithUser(Long uid_) {
		String uid = Long.toString(uid_);
		database.delete(DialogSQLiteHelper.TABLE_DIALOG, DialogSQLiteHelper.COLUMN_UID
					+ " = " + uid, null);
	}
	
	public void deleteChat(Long chat_id) {
		String uid = Long.toString(chat_id);
		database.delete(DialogSQLiteHelper.TABLE_DIALOG, DialogSQLiteHelper.COLUMN_CHAT_ID
				+ " = " + uid, null);
	}
	
	public void addMessageToDialog(ArrayList<Message> messages, Context context, String chat_id){
		ContentValues values = new ContentValues();
		for(Message message:messages){
			values.put(DialogSQLiteHelper.COLUMN_MID, message.mid);
			values.put(DialogSQLiteHelper.COLUMN_UID, message.uid);
			values.put(DialogSQLiteHelper.COLUMN_DATE, message.date);
			values.put(DialogSQLiteHelper.COLUMN_TITILE, message.title);
			values.put(DialogSQLiteHelper.COLUMN_BODY, message.body);
			values.put(DialogSQLiteHelper.COLUMN_READ_STATE, message.read_state);
			values.put(DialogSQLiteHelper.COLUMN_IS_OUT, message.is_out);
			values.put(DialogSQLiteHelper.COLUMN_CHAT_ID, chat_id);
			Log.d("chat_id insert", message.chat_id+"");
			if(message.attachments != null && message.attachments.size() != 0){	
				Log.d("attachments",message.attachments.toString());
		
				ArrayList<Photo> photos = new ArrayList<Photo>();
				ArrayList<Audio> audios = new ArrayList<Audio>();
				ArrayList<Video> videos = new ArrayList<Video>();
				ArrayList<Doc> docs = new ArrayList<Doc>();
				for(Attachment att:message.attachments){
					if(att.type.equals("photo")){
						photos.add(att.photo);
					}
					
					if(att.type.equals("video")){		
						videos.add(att.video);
					}
					
					if(att.type.equals("audio")){
						audios.add(att.audio);
					}
					
					if(att.type.equals("doc")){						
						docs.add(att.doc);
					}
				}
				if(photos.size()!=0){
					
					db_photos.deletePhotos(message.mid);
					db_photos.addPhoto(photos, message.mid);
					//db_photos.close();
				}
				
				if(videos.size()!=0){
					
					db_videos.deleteVideo(message.mid);
					db_videos.addVideo(videos, message.mid);
					//db_videos.close();
				}
				
				if(audios.size()!=0){
					
					db_audios.deleteAudio(message.mid);
					db_audios.addAudio(audios, message.mid);
					//db_audios.close();
				}
				
				if(docs.size()!=0){
					
					db_docs.deleteDocs(message.mid);
					db_docs.addDocs(docs, message.mid);
					//db_docs.close();
				}
				values.put(DialogSQLiteHelper.COLUMN_ATTACHMENTS, true);
			}
			else
				values.put(DialogSQLiteHelper.COLUMN_ATTACHMENTS, false);
			
			ArrayList<ForwardMessages> fwds = new ArrayList<ForwardMessages>();
			if(message.f_msgs != null && message.f_msgs.size()!=0){
				for(ForwardMessages fwd:message.f_msgs){
					fwds.add(fwd);
				}
				if(fwds != null && fwds.size()!=0){
					
					db_fwds.deleteFwd(message.mid);
					db_fwds.addFwds(fwds, message.mid);
					//db_fwds.close();					
				}	
				values.put(DialogSQLiteHelper.COLUMN_FMESSAGES, true);
			}
			else
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
	
	public ArrayList<Message> getChatDialog(Long chat_id, Context context) {
		ArrayList<Message> dialog = new ArrayList<Message>();
		
		String WHERE = DialogSQLiteHelper.COLUMN_CHAT_ID + " = " + Long.toString(chat_id);
		Log.d("chat_id getchatdialog", chat_id+"");

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
		Boolean fwds;
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
		fwds = (cursor.getInt(9) == 1);
		if(fwds)
			d.f_msgs = getFwds(d.mid, context);
		else
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
		ArrayList<Audio> audios = new ArrayList<Audio>();
		ArrayList<Video> videos = new ArrayList<Video>();
		ArrayList<Doc> docs = new ArrayList<Doc>();

		photos = getPhotos(mid, context);
		if(photos != null && photos.size() != 0)
			for(Photo photo:photos){
				Attachment attachment=new Attachment();
				attachment.type="photo";
				attachment.photo = photo;
				attachments.add(attachment);
			}
		
		audios = getAudios(mid, context);
		if(audios != null && audios.size() != 0)
			for(Audio audio:audios){
				Attachment attachment=new Attachment();
				attachment.type="audio";
				attachment.audio = audio;
				attachments.add(attachment);
			}
		videos = getVideos(mid, context);
		if(videos != null && videos.size() != 0)
			for(Video video:videos){
				Attachment attachment=new Attachment();
				attachment.type="video";
				attachment.video = video;
				attachments.add(attachment);
			}
		
		docs = getDocs(mid, context);
		if(docs != null && docs.size() != 0)
			for(Doc doc:docs){
				Attachment attachment=new Attachment();
				attachment.type="doc";
				attachment.doc = doc;
				attachments.add(attachment);
			}
		
		Log.d("attachments.size()", Integer.toString(attachments.size()));
		return attachments;
	}
	
	public ArrayList<Photo> getPhotos(String mid, Context context){
		ArrayList<Photo> photos = new ArrayList<Photo>();
		//db_photos = new PhotoDataSource(context);
		//db_photos.open();
		photos = db_photos.getAllPhotos(mid);
		//db_photos.close();
		return photos;
	}
	
	public ArrayList<Audio> getAudios(String mid, Context context){
		ArrayList<Audio> audios = new ArrayList<Audio>();
		//db_audios = new AudioDataSource(context);
		//db_audios.open();
		audios = db_audios.getAllAudios(mid);
		//db_audios.close();
		return audios;
	}
	
	public ArrayList<Video> getVideos(String mid, Context context){
		ArrayList<Video> videos = new ArrayList<Video>();
		//db_videos = new VideoDataSource(context);
		//db_videos.open();
		videos = db_videos.getAllVideos(mid);
		//db_videos.close();
		return videos;
	}
	
	public ArrayList<Doc> getDocs(String mid, Context context){
		ArrayList<Doc> docs = new ArrayList<Doc>();
		//db_docs = new DocDataSource(context);
		//db_docs.open();
		docs = db_docs.getAllDocs(mid);
		//db_docs.close();
		return docs;
	}
	
	public ArrayList<ForwardMessages> getFwds(String mid, Context context){
		ArrayList<ForwardMessages> fwds = new ArrayList<ForwardMessages>();
		fwds = getFwdsFromDB(mid, context);		
		Log.d("fwds.size()", Integer.toString(fwds.size()));
		return fwds;
	}
	
	public ArrayList<ForwardMessages> getFwdsFromDB(String mid, Context context){
		ArrayList<ForwardMessages> fwds = new ArrayList<ForwardMessages>();
		//db_fwds = new FwdDataSource(context);
		//db_fwds.open();
		fwds = db_fwds.getAllFwds(mid);
		//db_fwds.close();
		return fwds;
	}
}
