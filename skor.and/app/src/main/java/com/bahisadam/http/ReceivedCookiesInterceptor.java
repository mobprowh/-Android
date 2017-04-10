package com.bahisadam.http;

import com.bahisadam.utility.Preferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by atata on 30/12/2016.
 */

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {

            Preferences.getDefaultPreferences().edit().remove(Preferences.PREF_COOKIES).apply();
            String cookies ="";
            for (String header : originalResponse.headers("Set-Cookie")) {
                String cookie = header.split(";")[0];
                Preferences.getDefaultPreferences().edit().putString(cookie.split("=")[0],cookie.split("=")[1]).apply();
                cookies = header.split(";")[0] + ";" + cookies;
            }
        }

        return originalResponse;
    }
}