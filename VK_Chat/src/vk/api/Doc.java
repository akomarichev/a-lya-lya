package vk.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Doc {
    public long did;
    public long owner_id;
    public String title;
    public long size;
    public String ext;
    public String url;

    public static Doc parse(JSONObject o) throws NumberFormatException, JSONException{
        Doc doc = new Doc();
        doc.did = Long.parseLong(o.getString("did"));
        doc.owner_id = Long.parseLong(o.getString("owner_id"));
        doc.title = API.unescape(o.getString("title"));
        doc.size = Long.parseLong(o.getString("size"));
        doc.url = API.unescape(o.getString("url"));
        doc.ext = o.optString("ext", null);
        return doc;
    }
}