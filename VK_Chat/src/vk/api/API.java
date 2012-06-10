package vk.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class API {
	//private String url2 = "https://oauth.vk.com/token?grant_type=password&client_id=2985083&client_secret=powUs3vuKhIYNrgsLif1&username=calmnessart@mail.ru&password=nbvjynb";
	private String url = "https://oauth.vk.com/token";
	
	
	private String getSignedUrl(Params params) {
        String args = params.getParamsString();
        
        //add access_token
        //if(args.length()!=0)
        //    args+="&";
        //args+="access_token="+access_token;
        
        return url+params.method_name+"?"+args;
    }
    

	public String SendHttpPost() {
		try {
			Params params = new Params("");
	        params.put("grant_type", "password");
			params.put("client_id", "2985083");
			params.put("client_secret", "powUs3vuKhIYNrgsLif1");
			params.put("username", "calmnessart@mail.ru");
			params.put("password", "nbvjynb");
			
			String URL = getSignedUrl(params);
			
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
				resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

				return resultString;
				// Transform the String into a JSONObject
				//JSONObject jsonObjRecv = new JSONObject(resultString);
				// Raw DEBUG output of our received JSON object:
				//Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

				//return jsonObjRecv;
			} 

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
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
}
