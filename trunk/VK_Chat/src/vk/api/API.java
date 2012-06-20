package vk.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vk.utils.Base64;
import vk.utils.WrongResponseCodeException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
	
	private String getSignedUrlPOST(Params params, String URL) {
        String args = params.getParamsString(); 
        if(args.length()!=0)
            args+="&";
        args+="access_token="+access_token;
        return URL+"&"+args;
    }
	
	private final static int MAX_TRIES=3;
    private JSONObject sendRequest(Params params) throws IOException, MalformedURLException, JSONException{
        String url = getSignedUrl(params);
        Log.d("Artem2",url);
        String response="";
        for(int i=1;i<=MAX_TRIES;++i){
            try{
                response = sendRequestInternal(url);
                break;
            }catch(javax.net.ssl.SSLException ex){
                processNetworkException(i, ex);
            }catch(java.net.SocketException ex){
                processNetworkException(i, ex);
            }
        }
        JSONObject root=new JSONObject(response);
        return root;
    }
    
    private JSONObject sendPOSTRequest(String photo, String URL) throws IOException, MalformedURLException, JSONException{
        //String url = getSignedUrlPOST(params, URL);
        Log.d("Artem2",URL);
        String response="";
        

        MultipartEntity ent = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
        Bitmap bitmapOrg= BitmapFactory.decodeFile(photo);
        //Log.d("bitmap", bitmapOrg.toString());
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte [] ba = bao.toByteArray();
        //String ba1=Base64.encodeBytes(ba);
        ent.addPart("photo", new ByteArrayBody(ba,
                    "myImage.jpg"));
        
        //Log.d("ba1", ba);
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        httppost.setEntity(ent);
        /*ResponseHandler <String> res=new BasicResponseHandler();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("photo", ba1));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/

        for(int i=1;i<=MAX_TRIES;++i){
            try{
                HttpResponse response_post = httpclient.execute(httppost);
                HttpEntity entity = response_post.getEntity();
                if (entity != null) {
    				// Read the content stream
    				InputStream instream = entity.getContent();
    				// convert content stream to a String
    				String resultString= convertStreamToString(instream);
    				instream.close();
    				//resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

    				Log.d("API_post",resultString);
    				// Transform the String into a JSONObject
    				JSONObject jsonObjRecv = new JSONObject(resultString);
    				// Raw DEBUG output of our received JSON object:
    				//Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

    				return jsonObjRecv;
    			} 
                break;
            }catch(javax.net.ssl.SSLException ex){
                processNetworkException(i, ex);
            }catch(java.net.SocketException ex){
                processNetworkException(i, ex);
            }
        }
        JSONObject root=new JSONObject(response);
        return root;
    }
    
    
    private void processNetworkException(int i, IOException ex) throws IOException {
        ex.printStackTrace();
        if(i==MAX_TRIES)
            throw ex;
    }
    
    private String sendRequestInternal(String url) throws IOException, MalformedURLException, WrongResponseCodeException {
        HttpURLConnection connection=null;
        try{
            connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setUseCaches(false);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("Connection", "Keep-Alive");
            int code=connection.getResponseCode();
            //It may happen due to keep-alive problem http://stackoverflow.com/questions/1440957/httpurlconnection-getresponsecode-returns-1-on-second-invocation
            if (code==-1)
                throw new WrongResponseCodeException("Network error");
            InputStream is = new BufferedInputStream(connection.getInputStream(), 8192);
            String enc=connection.getHeaderField("Content-Encoding");
            if(enc!=null && enc.equalsIgnoreCase("gzip"))
                is = new GZIPInputStream(is);
            String response=convertStreamToString(is);
            Log.d("jfkdjfkjd",response);
            return response;
        }
        finally{
            if(connection!=null)
                connection.disconnect();
        }
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
        
        //JSONObject root = SendHttpPost(params); 
        JSONObject root = sendRequest(params);
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
        //JSONObject root = SendHttpPost(params);
        JSONObject root = sendRequest(params);
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
        //JSONObject root = SendHttpPost(params);
        JSONObject root = sendRequest(params);
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
    
    public String photosGetProfileUploadServer() throws MalformedURLException, IOException, JSONException{
        Params params = new Params("photos.getProfileUploadServer");
        //JSONObject root = SendHttpPost(params);
        JSONObject root = sendRequest(params);
        JSONObject response = root.getJSONObject("response");
        return response.getString("upload_url");
    }
    
    public String[] saveProfilePhoto(String server, String photo, String hash) throws MalformedURLException, IOException, JSONException{
        Params params = new Params("photos.saveProfilePhoto");
        params.put("server",server);
        params.put("photo",photo);
        params.put("hash",hash);
        //JSONObject root = SendHttpPost(params);
        JSONObject root = sendRequest(params);
        JSONObject response = root.getJSONObject("response");
        String src = response.optString("photo_src");
        String hash1 = response.optString("photo_hash");
        String[] res=new String[]{src, hash1};
        return res;
    }
    
    public String[] uploadPhotoServer(String url, String photo) throws MalformedURLException, IOException, JSONException{
    	Params params = new Params("");
    	params.put("photo",photo);
    	JSONObject root = sendPOSTRequest(photo, url);
        //JSONObject response = root.getJSONObject("response");
        String server = root.getString("server");
        String photos = root.getString("photo");
        String hash = root.getString("hash");
        String[] res=new String[]{server, photos, hash};
        return res;
    }
}
