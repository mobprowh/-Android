package com.bahisadam.model.requests;

import android.content.Context;
import android.os.Build;

import android.provider.Settings;
import android.util.Log;
import com.bahisadam.BuildConfig;
import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import static com.crashlytics.android.Crashlytics.TAG;

/**
 * Created by atata on 02/01/2017.
 */

public class DeviceInfo {
    String appVersion;
    public String bundleId;
    public String deviceId;
    public String deviceName;
    public String deviceOS;
    public String deviceToken;
    public String deviceVersion;
    public String devicModel;
    public DeviceInfo(){
        Context ctx = MyApplication.getAppContext();
        appVersion  = BuildConfig.VERSION_NAME;
        bundleId = ctx.getPackageName();

        deviceId  = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        /*deviceId  = "351" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10;*/
        deviceName = Build.DEVICE;
        deviceOS = "Android " + Build.VERSION.RELEASE;
/*
        try {
            InstanceID instanceID = InstanceID.getInstance(ctx);

            String token = instanceID.getToken(ctx.getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);

        }catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }*/

        deviceToken  = "";
        deviceVersion = Integer.toString(Build.VERSION.SDK_INT);
        devicModel = Build.MODEL;
    }


}
