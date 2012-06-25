package vk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Synch {
    public static Boolean press_synch;
    
    public static void save(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        Editor editor=prefs.edit();
        editor.putBoolean("press_synch", press_synch);
        editor.commit();
    }
    
    public static void restore(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        press_synch = prefs.getBoolean("press_synch", false);       
    }
    
    public static void clear(Context context){
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor=prefs.edit();
        editor.remove("press_synch").commit();
    }
}