package com.bahisadam;

import android.app.Application;
import android.content.Context;

import android.content.res.Configuration;
import com.bahisadam.interfaces.Constant;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Locale;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.messaging.FirebaseMessaging;
import io.fabric.sdk.android.Fabric;
import io.socket.client.IO;
import io.socket.client.Socket;


public class MyApplication extends Application {

    private static WeakReference<MyApplication> sInstance;
    private static WeakReference<Context> context;
    public static String sDefSystemLanguage;
    public static Calendar sCalendar;
    private Tracker mTracker;
    public static boolean sUse_Logo = true;

    private Socket mSocket;
    {
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = false;
            opts.transports = new String[] { "websocket"};
            opts.upgrade = true;

            mSocket = IO.socket(Constant.SOCKET_URL,opts);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    public Socket getSocket() {
        return mSocket;
    }

    public static MyApplication getInstance() {
        return sInstance.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sInstance = new WeakReference<>(this);
        MyApplication.context = new WeakReference<>(getApplicationContext());
        FirebaseMessaging.getInstance().subscribeToTopic("SkorAdam");
        sInstance.get().initializeInstance();
        sDefSystemLanguage = Locale.getDefault().getLanguage();
        sCalendar = Calendar.getInstance(Locale.getDefault());
    }

    private void initializeInstance() {

        // Initialize Fresco
        Fresco.initialize(this.getApplicationContext());
    }

    @Override
    public void onTerminate() {
        // Do your application wise Termination task
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        sDefSystemLanguage = newConfig.locale.getLanguage();
    }

    public static Context getAppContext() {
        return MyApplication.context.get();
    }
}
