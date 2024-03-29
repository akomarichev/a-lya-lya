package vk.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Link {
    public String url; 
    public String title; 
    public String description; 
    public String image_src;

    public static Link parse(JSONObject o) throws NumberFormatException, JSONException{
        Link link = new Link();
        link.url = o.optString("url");
        link.title = API.unescape(o.optString("title"));
        link.description = API.unescape(o.optString("description"));
        link.image_src = o.optString("image_src");
        return link;
    }
}
