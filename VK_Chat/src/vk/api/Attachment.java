package vk.api;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Attachment {
    public String type; //photo,posted_photo,video,audio,link,note,app,poll
    public Photo photo; 
    //public Photo posted_photo; 
    public Video video; 
    public Audio audio; 
    public Doc doc;

    public static ArrayList<Attachment> parseAttachments(JSONArray attachments, long from_id, long copy_owner_id) throws JSONException {
        ArrayList<Attachment> attachments_arr=new ArrayList<Attachment>();
        int size=attachments.length();
        for(int j=0;j<size;++j){
            Object att=attachments.get(j);
            if(att instanceof JSONObject==false)
                continue;
            JSONObject json_attachment=(JSONObject)att;
            Attachment attachment=new Attachment();
            attachment.type=json_attachment.getString("type");
            if(attachment.type.equals("photo") || attachment.type.equals("posted_photo")){
                JSONObject x=json_attachment.optJSONObject("photo");
                if(x!=null)
                    attachment.photo=Photo.parse(x);
            }
            if(attachment.type.equals("audio"))
                attachment.audio=Audio.parse(json_attachment.getJSONObject("audio"));
            if(attachment.type.equals("video"))
                attachment.video=Video.parse(json_attachment.getJSONObject("video"));
            if(attachment.type.equals("doc"))
            	attachment.doc=Doc.parse(json_attachment.getJSONObject("doc"));
            attachments_arr.add(attachment);
        }
        return attachments_arr;
    }
}
