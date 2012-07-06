package vk.pref;

import vk.constants.Constants;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Pref {
    public static String HAS_LOGGED_IN = "hasLoggedIn";   
    public static String HAS_SIGNUP = "hasSignUp";
    
    // DB
    public static String DB_FRIENDS = "dbFriendsLoaded";
    public static String DB_CONVERSATIONS = "dbFriendsConversations";
    public static String DB_CHAT = "dbChatLoaded";
    public static String DB_DIALOG = "dbDialogLoaded";
    private static String PREFS_NAME = "my_prefs";    
    
    // UPDATERS
    public static String UPD_CONVERSATIONS = "update_conversations";
    public static String UPD_CHAT_LIST_USER = "update_chat_list_users";
    public static String UPD_DIALOG = "update_dialog";
    
    // FRIENDS DB
	public static void setLoadedFriendsDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(DB_FRIENDS, true);
        editor.commit();
	}
	
    public static void cancelLoadedFriendsDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(DB_FRIENDS, false);
        editor.commit();
	}
    
	public static Boolean loadedFriendsDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
		final Editor editor=prefs.edit();
		return prefs.getBoolean(DB_FRIENDS, false);
	}    
	
    // dialogs DB
	public static void setLoadedDialogDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(DB_DIALOG, true);
        editor.commit();
	}
	
    public static void cancelLoadedDialogDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(DB_DIALOG, false);
        editor.commit();
	}
    
	public static Boolean loadedDialogDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
		final Editor editor=prefs.edit();
		return prefs.getBoolean(DB_DIALOG, false);
	}    

    // DB_CONVERSATIONS
	public static void setLoadedConversationsDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(DB_CONVERSATIONS, true);
        editor.commit();
	}
	
    public static void cancelLoadedConversationsDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(DB_CONVERSATIONS, false);
        editor.commit();
	}
    
	public static Boolean loadedConversationsDB(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
		final Editor editor=prefs.edit();
		return prefs.getBoolean(DB_CONVERSATIONS, false);
	}    
	
	// DB_CHAT
		public static void setLoadedChatDB(Context c){
			SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
	        Editor editor=prefs.edit();
	        editor.putBoolean(DB_CHAT, true);
	        editor.commit();
		}
		
	    public static void cancelLoadedChatDB(Context c){
			SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
	        Editor editor=prefs.edit();
	        editor.putBoolean(DB_CHAT, false);
	        editor.commit();
		}
	    
		public static Boolean loadedChatDB(Context c){
			SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
			final Editor editor=prefs.edit();
			return prefs.getBoolean(DB_CHAT, false);
		}   
    
    /* END WORK WITH DB */
    
	public static void logIn(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(HAS_LOGGED_IN, true);
        editor.commit();
	}
	
    public static void logOut(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(HAS_LOGGED_IN, false);
        editor.commit();
	}
    
	public static Boolean loggedIn(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
		final Editor editor=prefs.edit();
		return prefs.getBoolean(HAS_LOGGED_IN, false);
	}
	
	public static void signUp(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(HAS_SIGNUP, true);
        editor.commit();
	}
	
    public static void cancelSignUp(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(HAS_SIGNUP, false);
        editor.commit();
	}
    
	public static Boolean signedUp(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
		final Editor editor=prefs.edit();
		return prefs.getBoolean(HAS_SIGNUP, false);
	}
	
    public static void saveHTTPSAuth(Context c, String access_token, String user_id){
    	SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putString(Constants.ACCESS_TOKEN, access_token);
        editor.putString(Constants.USER_ID, user_id);
        editor.commit();
    }
    
    public static String getAccessTokenHTTPS(Context c){
    	SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        return prefs.getString(Constants.ACCESS_TOKEN, null);      
    }
    
    public static String getUserID(Context c){
    	SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        return prefs.getString(Constants.USER_ID, null);       
    }
    
    public static void deleteHTTPSAuth(Context c){
    	SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.remove(Constants.ACCESS_TOKEN);
        editor.remove(Constants.USER_ID);
        editor.commit();
    }
    
    // LONGPOLL
    
    public static void setLongPollServer(Context c, String key, String server, Long ts){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putString(Constants.LONGPOLL_KEY, key);
        editor.putString(Constants.LONGPOLL_SERVER, server);
        editor.putLong(Constants.LONGPOLL_TS, ts);
        editor.commit();
	}
	
    public static void outLongPollServer(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.remove(Constants.LONGPOLL_KEY);
        editor.remove(Constants.LONGPOLL_SERVER);
        editor.remove(Constants.LONGPOLL_TS);
        //editor.putBoolean(HAS_LOGGED_IN, false);
        editor.commit();
	}
    
	public static Object[] getLongPollServerParametrs(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
		String key=prefs.getString(Constants.LONGPOLL_KEY, null);
        String server=prefs.getString(Constants.LONGPOLL_SERVER, null);
        Long ts = prefs.getLong(Constants.LONGPOLL_TS, 0);
        return new Object[]{key, server, ts};
	}
    
    // END LONGPOLL
	
	
	// *************************** UPDATERS ******************************* //	
	public static void setNeedUpdateChatListActivity(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(UPD_CHAT_LIST_USER, true);
        editor.commit();
	}
	
    public static void resetNeedUpdateChatListActivity(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.putBoolean(UPD_CHAT_LIST_USER, false);
        editor.commit();
	}
    
	public static Boolean isSetNeedUpdateChatListActivity(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
		final Editor editor=prefs.edit();
		return prefs.getBoolean(UPD_CHAT_LIST_USER, false);
	} 
	
	public static void deleteNeedUpdateChatListActivity(Context c){
		SharedPreferences prefs = c.getSharedPreferences(PREFS_NAME, c.MODE_WORLD_READABLE|c.MODE_WORLD_WRITEABLE);
        Editor editor=prefs.edit();
        editor.remove(UPD_CHAT_LIST_USER);
        editor.commit();
	}
	
	
	// *************************** END UPDATERS ******************************* //
}
