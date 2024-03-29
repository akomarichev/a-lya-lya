package vk.api;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    //public long pid;
    //public long aid;
    //public String owner_id;
    public String src;
    //public String src_small;//not used for now because in newsfeed it's empty
    //public String src_big;
    public String src_xbig;
    //public String src_xxbig;
    public String phototext;
    //public long created;
    //public Integer like_count;
    //public Boolean user_likes;

    public static Photo parse(JSONObject o) throws NumberFormatException, JSONException{
        Photo p = new Photo();
        //p.pid = o.getLong("pid");
        //p.aid = o.optLong("aid");
        //p.owner_id = o.getString("owner_id");
        p.src = o.getString("src");
        //p.src_small = o.optString("src_small");
        //p.src_big = o.getString("src_big");
        p.src_xbig = o.optString("src_xbig");
        //p.src_xxbig = o.optString("src_xxbig");
        p.phototext = API.unescape(o.optString("text"));
        //p.created = o.optLong("created");
        
        /*if (o.has("likes")){
            JSONObject jlikes = o.getJSONObject("likes");
            p.like_count = jlikes.getInt("count");
            p.user_likes = jlikes.getInt("user_likes")==1;
        }*/
        return p;
    }

    public Photo(){
    }

    /*public Photo(Long id, String owner_id, String src, String src_big){
        this.pid=id;
        this.owner_id=owner_id;
        this.src=src;
        this.src_big=src_big;
    }*/
}
