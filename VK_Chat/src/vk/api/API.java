package vk.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;

public class API {
	//private String url2 = "https://oauth.vk.com/token?grant_type=password&client_id=2985083&client_secret=powUs3vuKhIYNrgsLif1&username=calmnessart@mail.ru&password=nbvjynb";
	private String url = "https://api.vk.com/method/";
	private String access_token="3c8616a539ec404439ec4044c439c1cc3f339ec39eb7cbb3dc0143b2f81d413";
	private String user_id = "90855137";
	
	
	private String getSignedUrl(Params params) {
        String args = params.getParamsString();
        
        //add access_token
        if(args.length()!=0)
            args+="&";
        args+="access_token="+access_token;
        
        return url+params.method_name+"?"+args;
    }
    

	public JSONObject SendHttpPost(Params params) {
		try {
			/*Params params = new Params("");
			params.put("scope", Auth.getSettings());
	        params.put("grant_type", "password");
			params.put("client_id", "2985083");
			params.put("client_secret", "powUs3vuKhIYNrgsLif1");
			params.put("username", "calmnessart@mail.ru");
			params.put("password", "nbvjynb");*/
			
			
			String URL = getSignedUrl(params);
			
			Log.d("Artem", URL);
			
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL);

			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Content-type", "application/json");
			httpGet.setHeader("Accept-Encoding", "gzip");

			HttpResponse response = (HttpResponse) httpclient.execute(httpGet);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				// convert content stream to a String
				String resultString= convertStreamToString(instream);
				instream.close();
				//resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

				Log.d("API",resultString);
				// Transform the String into a JSONObject
				JSONObject jsonObjRecv = new JSONObject(resultString);
				// Raw DEBUG output of our received JSON object:
				//Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

				return jsonObjRecv;
			} 

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}	
	
	public static String unescape(String text){
        return Html.fromHtml(text).toString();
    }
	
	public ArrayList<User> getFriends() throws MalformedURLException, IOException, JSONException{
        Params params = new Params("friends.get");
        String fields="first_name,last_name,photo_rec,online,contacts";
        params.put("fields",fields);
        params.put("uid",user_id);
        params.put("order","hints");
        //params.put("scope", Auth.getSettings());
        
        JSONObject root = SendHttpPost(params);
        ArrayList<User> users=new ArrayList<User>();
        JSONArray array=root.optJSONArray("response");
        //if there are no friends "response" will not be array
        if(array==null)
            return users;
        int category_count=array.length();
        for(int i=0; i<category_count; ++i){
            JSONObject o = (JSONObject)array.get(i);
            User u = User.parse(o);
            users.add(u);
        }
        return users;
    }
	
	
    public ArrayList<Message> getMessagesDialogs(long time_offset, int count) throws MalformedURLException, IOException, JSONException{
        Params params = new Params("messages.getDialogs");
        if (time_offset!=0)
            params.put("time_offset", time_offset);
        if (count != 0)
            params.put("count", count);
        params.put("preview_length","0");
        JSONObject root = SendHttpPost(params);
        JSONArray array = root.optJSONArray("response");
        ArrayList<Message> messages = parseMessages(array, false, 0, false);
        return messages;
    }
    
    public ArrayList<Message> getMessagesHistory(long uid, long chat_id, Long offset, int count) throws MalformedURLException, IOException, JSONException{
        Params params = new Params("messages.getHistory");
        if(chat_id<=0)
            params.put("uid",uid);
        else
            params.put("chat_id",chat_id);
        params.put("offset", offset);
        if (count != 0)
            params.put("count", count);
        JSONObject root = SendHttpPost(params);
        JSONArray array = root.optJSONArray("response");
        ArrayList<Message> messages = parseMessages(array, chat_id<=0, uid, chat_id>0);
        return messages;
    }
    
    private ArrayList<Message> parseMessages(JSONArray array, boolean from_history, long history_uid, boolean from_chat) throws JSONException {
        ArrayList<Message> messages = new ArrayList<Message>();
        if (array != null) {
            int category_count = array.length();
            for(int i = 1; i < category_count; ++i) {
                JSONObject o = (JSONObject)array.get(i);
                Message m = Message.parse(o, from_history, history_uid, from_chat);
                messages.add(m);
            }
        }
        return messages;
    }
}
