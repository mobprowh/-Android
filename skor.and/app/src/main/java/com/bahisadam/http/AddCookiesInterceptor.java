package com.bahisadam.http;

/**
 * Created by atata on 29/12/2016.
 */

import com.bahisadam.utility.Preferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();


        String affinity = Preferences.getDefaultPreferences().getString(Preferences.PREF_COOKIE_HEROKU_AFFINITY,"");
        String sid = Preferences.getDefaultPreferences().getString(Preferences.PREF_COOKIE_SID,"");

        String cookies = Preferences.PREF_COOKIE_HEROKU_AFFINITY + "=" + affinity + ";"
                +Preferences.PREF_COOKIE_SID + "=" + sid;
        builder.addHeader("Cookie",cookies);

        return chain.proceed(builder.build());
    }
}