package vk.api;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ForwardMessages {
    public String date;
    public long uid;

    public String title;
    public String body;

    public static ArrayList<ForwardMessages> parseForwardMessages(JSONArray f_msgs) throws JSONException {
    	Log.d("Forward M", f_msgs.toString());
        ArrayList<ForwardMessages> f_msgs_arr=new ArrayList<ForwardMessages>();
        int size=f_msgs.length();
        for(int j=0;j<size;++j){
            Object f_m=f_msgs.get(j);
            if(f_m instanceof JSONObject==false)
                continue;
            JSONObject json_f_m=(JSONObject)f_m;
            ForwardMessages f_msg = new ForwardMessages();
            f_msg.uid = json_f_m.getLong("uid");
            f_msg.body = json_f_m.getString("body");
            f_msg.date = json_f_m.getString("date");
            f_msgs_arr.add(f_msg);
        }
        return f_msgs_arr;
    }
}
