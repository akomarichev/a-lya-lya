package vk.api;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Message {
    public String date;
    public long uid;
    //TODO make long
    public String mid;
    public String title;
    public String body;
    //TODO make boolean
    public String read_state;
    public boolean is_out;
    public ArrayList<Attachment> attachments=new ArrayList<Attachment>();
    public ArrayList<ForwardMessages> f_msgs = new ArrayList<ForwardMessages>();
    public Long chat_id;

    public static Message parse(JSONObject o, boolean from_history, long history_uid, boolean from_chat) throws NumberFormatException, JSONException{
        Message m = new Message();
        if(from_chat){
            long from_id=o.getLong("from_id");
            m.uid = from_id;
            m.is_out=(from_id!=history_uid);
        }else if(from_history){
            m.uid=history_uid;
            Long from_id = o.getLong("from_id");
            m.is_out=!(from_id==history_uid);
        }else{
            //тут не очень, потому что при получении списка диалогов если есть моё сообщение, которое я написал в беседу, то в нём uid будет мой. Хотя в других случайх uid всегда собеседника.
            m.uid = o.getLong("uid");
            m.is_out = o.getInt("out")==1;
        }
        m.mid = o.getString("mid");
        m.date = o.getString("date");
        if(!from_history && !from_chat)
            m.title = API.unescape(o.getString("title"));
        m.body = API.unescape(o.getString("body"));
        m.read_state = o.getString("read_state");
        if(o.has("chat_id")){
        	m.chat_id=o.getLong("chat_id");
        	Log.d("chat_id_messages", m.chat_id+"");
        }
            

        JSONArray attachments=o.optJSONArray("attachments");
        if(attachments!=null)
            m.attachments=Attachment.parseAttachments(attachments, 0, 0);
        
        JSONArray f_messages=o.optJSONArray("fwd_messages");
        if(f_messages!=null){
        	//Log.d("Message", o.toString());
        	//Log.d("Message", f_messages.toString());
            m.f_msgs=ForwardMessages.parseForwardMessages(f_messages);
            Log.d("Message", m.f_msgs.toString());           
        }
        return m;
    }

    public static int UNREAD = 1;	 	//сообщение не прочитано 
    public static int OUTBOX = 2;	 	//исходящее сообщение 
    public static int REPLIED = 4;	 	//на сообщение был создан ответ 
    public static int IMPORTANT = 8; 	//помеченное сообщение 
    public static int CHAT = 16;    	//сообщение отправлено через диалог
    public static int FRIENDS = 32;		//сообщение отправлено другом 
    public static int SPAM = 64;		//сообщение помечено как "Спам"
    public static int DELETED = 128;	//сообщение удалено (в корзине)
    public static int FIXED = 256; 		//сообщение проверено пользователем на спам 
    public static int MEDIA = 512;		//сообщение содержит медиаконтент
    public static int BESEDA = 8192;    //беседа

    public static Message parse(JSONArray a) throws JSONException {
        Message m = new Message();
        m.mid = a.getString(1);
        m.uid = a.getLong(3);
        m.date = a.getString(4);
        m.title = API.unescape(a.getString(5));
        m.body = API.unescape(a.getString(6));
        int flag = a.getInt(2);
        m.read_state = ((flag & UNREAD) != 0)?"0":"1";
        m.is_out = (flag & OUTBOX) != 0;
        if ((flag & BESEDA) != 0) {
            m.chat_id = a.getLong(3) & 63;//cut 6 last digits
            JSONObject o= a.getJSONObject(7);
            m.uid = o.getLong("from");
        }
        //m.attachment = a.getJSONArray(7); TODO			 
        return m;
    }
}
