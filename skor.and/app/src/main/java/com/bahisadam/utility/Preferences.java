package com.bahisadam.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.bahisadam.MyApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by atata on 29/12/2016.
 */

public class Preferences {
    static Preferences  mInstance;
    public static final String PREF_COOKIES = "COOKIE";

    public static final String PREF_COOKIE_SID = "connect.sid";
    public static final String PREF_COOKIE_HEROKU_AFFINITY = "heroku-session-affinity";
    private static final String preferenceFile = "SkorAdamPrefs";
    public static final String PREF_USER = "USER";
    public static final String PREF_LOGGED = "LOGGED";
    public static final String PREF_FAVORITES = "FAVORITES";

    public static SharedPreferences getDefaultPreferences(){
        Context ctx = MyApplication.getAppContext();
        SharedPreferences prefs = ctx.getSharedPreferences(preferenceFile,Context.MODE_PRIVATE);
        return  prefs;
    }

    public static String getUser() {
        String user = getDefaultPreferences().getString(PREF_USER,null);
        return user;
    }
    public static void setUser(String user){
        getDefaultPreferences()
                .edit()
                .putString(PREF_USER,user)
                .apply();
    }
    public static void setIsLogged(boolean logged){
        getDefaultPreferences().edit().putBoolean(PREF_LOGGED,logged).apply();
        if(!logged)
            getDefaultPreferences()
                    .edit()
                    .putString(PREF_USER,null)
                    .apply();
    }
    public static boolean isLogged(){
        //return getDefaultPreferences().getBoolean(PREF_LOGGED,false);
        return getUser()!=null;
    }
    public static Set<String> getFavoriteMatches(){
        return getDefaultPreferences().getStringSet(PREF_FAVORITES, new HashSet<String>());
    }



}
