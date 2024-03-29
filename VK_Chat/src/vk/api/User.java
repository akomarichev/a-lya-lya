package vk.api;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public long uid;
    public String first_name;
    public String last_name;
    public Boolean online;
    public String photo_rec; 
    public String mobile_phone;
    public String photo_medium;
    
    public static User parse(JSONObject o) throws JSONException {
        User u = new User();
        u.uid = Long.parseLong(o.getString("uid"));
        if(!o.isNull("first_name"))
            u.first_name = API.unescape(o.getString("first_name"));
        if(!o.isNull("last_name"))
            u.last_name = API.unescape(o.getString("last_name"));
        if(!o.isNull("online"))
            u.online = o.optInt("online")==1;
        if(!o.isNull("photo_rec"))
            u.photo_rec = o.optString("photo_rec");
        if(!o.isNull("phone"))
            u.mobile_phone = o.optString("phone");
        if(!o.isNull("photo_medium"))
            u.photo_medium = o.optString("photo_medium");
        return u;
    }
    
    public String getFullName(){
    	return first_name + " " + last_name;
    }
}
