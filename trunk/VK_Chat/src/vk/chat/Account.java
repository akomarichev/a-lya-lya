package vk.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Account {
    public static String access_token;
    public static long user_id;
    
    public static void save(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor=prefs.edit();
        editor.putString("access_token", access_token);
        editor.putLong("user_id", user_id);
        editor.commit();
    }
    
    public static void restore(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        access_token=prefs.getString("access_token", null);
        user_id=prefs.getLong("user_id", 0);
        
    }
}
