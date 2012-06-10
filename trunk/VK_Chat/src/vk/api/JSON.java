//package vk.api;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//public class JSON {
//
//	/**
//	 * @param args
//	 */
//	private String URL = "http://schetovvod.ru/public/login";
//	
//	/*public static void main(String[] args) {
//		matchJSON();
//	}*/
//		
//	public JSONObject sendHttpPost(String URL, JSONObject jsonObjSend){
//		try{
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPostRequest = new HttpPost(URL);
//			int statusCode;
//			
//			StringEntity se;
//			se = new StringEntity(jsonObjSend.toJSONString());
//			
//			httpPostRequest.setEntity(se);
//			httpPostRequest.setHeader("Accept", "application/json");
//			httpPostRequest.setHeader("Content-type", "application/json");
//			
//			HttpResponse response = httpClient.execute(httpPostRequest);
//			
//			HttpEntity entity = response.getEntity();
//			
//			statusCode = response.getStatusLine().getStatusCode();
//			
//			JSONParser parser = new JSONParser();
//			//JSONObject jsonObjRecv = (JSONObject) parser.parse(EntityUtils.toString(entity));
//			
//		
//			
//			if(entity != null){
//				InputStream inStream = entity.getContent();
//				
//				String resultString = convertStreamToString(inStream);
//				inStream.close();
//				
//				//resultString = resultString.substring(1, resultString.length()-1);
//				JSONObject jsonObjRecv = new JSONObject();
//				jsonObjRecv = (JSONObject)new JSONParser().parse(resultString);
//				
//				//return jsonObjRecv;
//				
//				System.out.println(jsonObjRecv);
//				System.out.println(statusCode);
//				System.out.println(resultString.toString());
//				
//				return jsonObjRecv;
//			}
//					
//			
//		} catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	private String convertStreamToString(InputStream is){
//		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//		StringBuilder sb = new StringBuilder();
//		String line = null;
//		
//		try{
//			while((line = reader.readLine()) != null)
//				sb.append(line + "\n");
//		} catch(IOException e){
//			e.printStackTrace();
//		} finally {
//			try{
//				is.close();
//			} catch (IOException e){
//				e.printStackTrace();
//			}
//		}
//		
//		return sb.toString();
//	}
//	
//	public int matchJSON(){
//		JSONObject jsonObjSend = new JSONObject();		
//		
//		jsonObjSend.put("login", "1000000000");
//		jsonObjSend.put("password", "111111");
//		System.out.println(jsonObjSend);
//		
//		JSONObject jsonObjRecv = sendHttpPost(URL, jsonObjSend);
//		
//		if(jsonObjRecv == null)
//			return 0;
//		
//		if(
//				jsonObjRecv.containsKey("id") == true &&
//				jsonObjRecv.containsKey("limit") == true &&
//				jsonObjRecv.containsKey("token") == true &&
//				jsonObjRecv.containsKey("territory") == true &&
//				jsonObjRecv.containsKey("email") == true &&
//				jsonObjRecv.containsKey("activationCodes") == true &&
//				jsonObjRecv.containsKey("messages") == true &&
//				jsonObjRecv.containsKey("cards") == true &&
//				jsonObjRecv.containsKey("phone") == true 	
//				)
//			return 1;
//		
//		return 0;
//	}
//}
