package com.bahisadam.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bahisadam.Cache;
import com.bahisadam.MyApplication;
import com.bahisadam.R;
import com.bahisadam.http.AddCookiesInterceptor;
import com.bahisadam.http.ReceivedCookiesInterceptor;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.interfaces.RestClient;
import com.bahisadam.model.MatchPOJO;
import com.bahisadam.model.ToolbarItem;
import com.bahisadam.model.WeeksModel;
import com.bahisadam.model.requests.AddRemoveFavoriteRequest;
import com.bahisadam.model.requests.BaseAuthRequest;
import com.bahisadam.model.requests.DeviceLoginRequest;
import com.bahisadam.model.requests.UserLoginRequest;
import com.bahisadam.view.BaseActivity;
import com.bahisadam.view.DetailPageActivity;
import com.bahisadam.view.TeamDetailsActivity;
import com.bahisadam.view.HomeActivity;
import com.bahisadam.view.LeagueDetailsActivity;
import com.bahisadam.view.RegistrationActivity;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.id;
import static android.R.attr.progress;
import static android.R.attr.timeZone;
import static com.bahisadam.R.drawable.tr;
import static com.bahisadam.view.BaseActivity.PAGE;
import static com.bahisadam.view.BaseActivity.REQUEST;
import static com.crashlytics.android.Crashlytics.TAG;


public class Utilities implements Constant {

    static ArrayList<String> weekStr;
    static final String offset = "03:00.000Z";

    // Set Font TypeFace
    public static void setTypeFace(Context ctx, TextView tv) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath);
        tv.setTypeface(tf);
    }

    public static RestClient buildRetrofit() {
        return buildRetrofit(false);
    }

    public static RestClient buildRetrofit(boolean saveCookie) {
        Gson gson = new GsonBuilder()
                .create();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor());
        if (saveCookie) {
            builder.addInterceptor(new ReceivedCookiesInterceptor());

        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        return retrofit.create(RestClient.class);
    }

    // Set Font TypeFace
    public static void setTypeFace2(Context ctx, TextView tv) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath2);
        tv.setTypeface(tf);
    }

    /* Check Internet Available Or Not */
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String truncateTeamName(String name) {
        if(name == null) return "";
        return name.length() > 10 ? name.substring(0, 10) + "..." : name;
    }

    // Pass Next Activity Intent
    public static void passActivityIntent(Context ctx, Class nextActivity) {
        Intent intent = new Intent(ctx, nextActivity);

//        if (nextActivity.getName().toString().equals(activityName2)) {
//            intent.putExtra(SNAP_PIC, HomeFragment.getInstance().getImage());
//            intent.putExtra(KEY_USER_ID, HomeFragment.getInstance().getUserId());
//            intent.putExtra(KEY_IMAGE_TIME, HomeFragment.getInstance().getImageTime());
//        }
//
//        if (nextActivity.getName().toString().equals(activityName)) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        }
        ctx.startActivity(intent);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /* Show SnackBar */
    public static void showSnackBar(Context ctx, View view, String message) {
        if (ctx == null || view == null) return;
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View views = snackbar.getView();
        views.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimary));
        TextView tv = (TextView) views.findViewById(android.support.design.R.id.snackbar_text);
        //setTypeFace(ctx, tv);
        tv.setTextColor(ContextCompat.getColor(ctx, R.color.md_white_1000));
        snackbar.show();
    }


    /* Show SnackBar With Call Back*/
    public static void showSnackBarWithCallBack(final Context ctx, View view, String message,
                                                final Class nextActivity) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View views = snackbar.getView();
        views.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimary));
        TextView tv = (TextView) views.findViewById(android.support.design.R.id.snackbar_text);
        setTypeFace(ctx, tv);
        tv.setTextColor(ContextCompat.getColor(ctx, R.color.md_white_1000));
        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                Utilities.passActivityIntent(ctx, nextActivity);
                ((Activity) ctx).finish();
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        });
        snackbar.show();
    }

    /* Show SnackBar With Call Back*/
    public static void showSnackBarWithCallBackLoadFragment(final Context ctx, View view, String message,
                                                            final int position) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View views = snackbar.getView();
        views.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimary));
        TextView tv = (TextView) views.findViewById(android.support.design.R.id.snackbar_text);
        setTypeFace(ctx, tv);
        tv.setTextColor(ContextCompat.getColor(ctx, R.color.md_white_1000));
        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {

            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        });
        snackbar.show();
    }

    /* Show SnackBar For Internet */
    public static void showSnackBarInternet(Context ctx, View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT)
                .setAction(ctx.getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimary));

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        //setTypeFace(ctx, textView);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    /* Show Progress Dialog */
    public static void showProgressDialog(Context ctx, ProgressBar progressBar) {
        if (progressBar == null) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ((Activity) ctx).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /* Dismiss Progress Dialog */
    public static void dismissProgressDialog(Context ctx, ProgressBar progressBar) {
        if (ctx == null || progressBar == null)
            return;
        progressBar.setVisibility(View.GONE);
        ((Activity) ctx).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public static String getResponse(RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    //    "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
//    "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
//            "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
//            "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
//            "yyMMddHHmmssZ"-------------------- 010704120856-0700
//            "K:mm a, z" ----------------------- 0:08 PM, PDT
//    "h:mm a" -------------------------- 12:08 PM
//    "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
    public static String getCurrentDateTime() {
        Locale trlocale = new Locale("tr-TR");
        Locale enLocale = new Locale("en_US");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", trlocale);
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime()); // compute start of the day for the timestamp
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String date = df.format(cal.getTime());
        return date;
    }


    public static String getNextDate(String curDate) {
        Locale trlocale = new Locale("tr-TR");
        Locale enLocale = new Locale("en_US");
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", trlocale);
        final Date date;
        Calendar calendar = null;
        try {
            date = format.parse(curDate);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format.format(calendar.getTime());
    }

    /* Format Date From String */
    public static String formatDate(String inputDate) {
        Date parsed = null;
        String outputDate = "";
        String inputFormat = "yyyy-MM-dd'T'hh:mm";
        String outputFormat = "yyyy.MM.dd";
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(removeLastChar(inputDate));
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("ParseException", "ParseException??" + e.getMessage());
        }

        return outputDate;

    }

    public static Date parseJSONDate(String dateStr) {
        Date d = null;
        DateFormat fmt;
        try {
            fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            d = fmt.parse(dateStr);
        } catch (ParseException e) {
            Log.i("ParseException 1", "Invalid JSON Date : " + e.getMessage());
        }

        if(d == null) {
            try {
                fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ssZ");
                d = fmt.parse(dateStr);
            } catch (ParseException e) {
                Log.i("ParseException 2", "Invalid JSON Date : " + e.getMessage());
            }
        }
        if(d == null) {
            try {
                fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                d = fmt.parse(dateStr);
            } catch (ParseException e) {
                Log.i("ParseException 3", "Invalid JSON Date : " + e.getMessage());
            }
        }


        if(d == null) {
            Log.e("ParseException", "Invalid JSON Date");
        }

        return d;
    }

    /* Format Date  */
    public static String formatDate(Date date, String format) {
        String result = null;
        try {
            SimpleDateFormat spf = new SimpleDateFormat(format);
            spf.setTimeZone(MyApplication.sCalendar.getTimeZone());
            result = spf.format(date);
        } catch (Exception e) {
            Log.e("ParseException", "ParseException??" + e.getMessage());
        }

        return result;
    }

    public static String toJSONDateString(Date date) {
        return formatDate(date, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    /*
    public static Locale getCurrentLocale() {
        Locale current = getResources().getConfiguration().locale;
    }*/

    /* Format Date From String */
    public static String formatDate(String inputDate, String outputFormat) {
        Date parsed = null;
        String outputDate = "";
        String inputFormat = "yyyy-MM-dd'T'hh:mm";
        Locale locale = new Locale(MyApplication.sDefSystemLanguage);
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, locale);
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, locale);

        try {
            parsed = df_input.parse(removeLastChar(inputDate));
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("ParseException", "ParseException??" + e.getMessage());
        }

        return outputDate;

    }

    /* Format Date From String */
    public static String formatTime(String inputDate) {
        Date parsed = null;
        String outputDate = "";
        String inputFormat = "yyyy-MM-dd'T'hh:mm";
        String outputFormat = "HH:mm";
        Locale trlocale = new Locale("tr-TR");
        Locale enLocale = new Locale("en_US");
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, trlocale);
        df_input.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, trlocale);

        try {
            parsed = df_input.parse(removeLastChar(inputDate));
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("ParseException", "ParseException??" + e.getMessage());
        }

        return outputDate;

    }

    private static String removeLastChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 8);
        }
        return str;
    }


    /* Show Setting Alert */
    public static void showSettingsAlert(final Context ctx) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(ctx)
//                .title(ctx.getString(R.string.settings))
//                .content(ctx.getString(R.string.message_gps))
//                .typeface(tf, tf)
//                .positiveText(ctx.getString(R.string.settingss))
//                .negativeText(ctx.getString(R.string.cancel))
                ;

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(intent);
                dialog.dismiss();

            }
        })

                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }


    public static Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set( Calendar.AM_PM, Calendar.AM );
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getUTCToday() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT0"));
        cal.set(Calendar.ZONE_OFFSET, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set( Calendar.AM_PM, Calendar.AM );
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date addDay(Date d, int day) {
        return new Date(day * 24 * 60 * 60 * 1000 + d.getTime());
    }

    public static Date addHour(Date d, int hour) {
        return new Date((hour * 60 * 60 * 1000) + d.getTime());
    }

    public static Date addMinute(Date d, int minute) {
        return new Date(minute * 60 * 1000 + d.getTime());
    }

    public static WeeksModel createWeekModel(Calendar cal, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d", locale);
        WeeksModel weeksModel = new WeeksModel();
        String[] parts = sdf.format(cal.getTime()).split(" ");
        String part1 = parts[0];
        String part2 = parts[1];

        weeksModel.setWeeks(part1);
        weeksModel.setDays(part2);
        return weeksModel;
    }

    public static ArrayList<WeeksModel> getCurrentWeek(Context ctx, String dateSelected) {
        ArrayList<WeeksModel> weeks = new ArrayList<>();
        weekStr = new ArrayList<>();

        Locale trlocale = new Locale(MyApplication.sDefSystemLanguage);
        Date date = null;
        Calendar cal = Calendar.getInstance(trlocale);
        date = Utilities.parseJSONDate(dateSelected);
        cal.setTime(date);

        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        //walk backward
        cal.add(Calendar.DAY_OF_MONTH, -1);

        while(weekStr.size() < 7) {
            WeeksModel dayModel = createWeekModel(cal, trlocale);
            dayModel.setCurrentDate(currentDayOfMonth == cal.get(Calendar.DAY_OF_MONTH));
            weeks.add(dayModel);

            weekStr.add(Utilities.toJSONDateString(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        WeeksModel wm = new WeeksModel();
        wm.setWeeks(ctx.getString(R.string.cal_name));
        wm.setDays("");
        weeks.add(wm);
        return weeks;
    }

    private static String getString(Context ctx, String string){
            String packageName = ctx.getPackageName();
            int resId = ctx.getResources().getIdentifier(string, "string", packageName);
            return ctx.getString(resId);

    }
    
    public static int getPx(Context activity,int dimensionDp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

    @Deprecated
    public static ArrayList<WeeksModel> getCurrentWeekOld(Context ctx) {
        ArrayList<WeeksModel> weeks = new ArrayList<>();
        weekStr = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.DAY_OF_WEEK, cal2.getFirstDayOfWeek());
        cal2.set(Calendar.HOUR_OF_DAY, 3);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        Locale trlocale = new Locale("tr-TR");
        Locale enLocale = new Locale("en_US");

        SimpleDateFormat sdf = new SimpleDateFormat("EEE d", trlocale);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", trlocale);

        SimpleDateFormat df = new SimpleDateFormat("d", trlocale);
        String date = df.format(Calendar.getInstance().getTime());

        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_WEEK, 1);
            cal2.add(Calendar.DAY_OF_WEEK, 1);
            weekStr.add(sdf2.format(cal2.getTime()));

            WeeksModel weeksModel = new WeeksModel();
            String[] parts = sdf.format(cal.getTime()).split(" ");
            String part1 = parts[0];
            String part2 = parts[1];

            weeksModel.setWeeks(part1);
            weeksModel.setDays(part2);

            if (part2.equals(date)) {
                weeksModel.setCurrentDate(true);
            } else {
                weeksModel.setCurrentDate(false);
            }
            weeks.add(weeksModel);
        }
        WeeksModel weeksModel = new WeeksModel();
        weeksModel.setWeeks(ctx.getString(R.string.cal_name));
        weeksModel.setDays("");
        weeks.add(weeksModel);

        return weeks;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    // Get Array List For Dates...
    public static ArrayList<String> getDateList() {
        return weekStr;
    }

    private static int parseColor(String color) {
        int parsed = -1;
        try {
            if (color != null && !color.isEmpty()) {
                color = color.toLowerCase();
                color = color.trim();

                if (color.equals("0"))
                    color = "#ffffff";
                else if (color.equals("red"))
                    parsed = Color.RED;
                else if (color.equals("yellow"))
                    parsed = Color.YELLOW;
                else if (color.equals("black"))
                    parsed = Color.BLACK;
                else if (color.equals("white"))
                    parsed = Color.WHITE;
                parsed = Color.parseColor(color);
            }
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
            Crashlytics.log("Color unable to parse: " + color);
            Log.e("ColorParse", "Color unable to parse: " + color);
        } catch (StringIndexOutOfBoundsException e) {
            //e.printStackTrace();
            Log.e("ColorParse", "Color has no data color1: " + color);
            Crashlytics.log("Color has no data color " + color);
        }
        return parsed;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap paintTeamIcon(Context ctx, String color1, String color2) {
        int color1parsed = parseColor(color1);
        int color2parsed = color2 == null || color2.equals("0") ? parseColor(color1) : parseColor(color2);
        if (ctx == null) return null;
        VectorDrawable vectorDrawable = (VectorDrawable) ctx.getResources().getDrawable(R.drawable.ic_team_logo);
        //  Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
        //            vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap = Bitmap.createBitmap(70, 70, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        for (int i = 0; i < bitmap.getWidth(); i++)
            for (int j = 0; j < bitmap.getHeight(); j++) {
                if (bitmap.getPixel(i, j) == -5668222) {
                    bitmap.setPixel(i, j, color2parsed);
                } else if (bitmap.getPixel(i, j) == -13475908) {
                    bitmap.setPixel(i, j, color1parsed);
                }
            }
        return bitmap;
    }

    public static Calendar parseDate(String dateString) {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        Date date = null;
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        try {
            date = sdf3.parse(dateString.substring(0, dateString.length() - 1));
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static List<ToolbarItem> createToolbarItems(Context ctx) {
        List<ToolbarItem> toolbarItems = new ArrayList<ToolbarItem>();
        ToolbarItem matches = new ToolbarItem();
        matches.height = 28;
        matches.width = 28;
        matches.img = "ic_menu_item_1";
        matches.name = ctx.getString(R.string.matches);
        toolbarItems.add(matches);
        ToolbarItem standings = new ToolbarItem();
        standings.height = 28;
        standings.width = 28;
        standings.name = ctx.getString(R.string.puan);
        standings.img = "ic_puan";
        toolbarItems.add(standings);


        ToolbarItem live = new ToolbarItem();
        live.height = 28;
        live.width = 28;
        live.img = "ic_live_scores";
        live.name = ctx.getString(R.string.live);
        toolbarItems.add(live);

        ToolbarItem stats = new ToolbarItem();
        stats.height = 28;
        stats.width = 28;
        stats.img = "ic_favorites";
        stats.name = ctx.getString(R.string.toolbar_favorite);
        toolbarItems.add(stats);

        ToolbarItem turkey = new ToolbarItem();
        turkey.width = 42;
        turkey.height = 40;
        turkey.name = "";
        //turkey.name = ctx.getString(R.string.turkey);
        //turkey.img="ic_menu_item_5";
        turkey.img = "superlig_logo";
        toolbarItems.add(turkey);
        return toolbarItems;
    }

    public static void openLeagueDetails(Activity activity, int leagueId) {
        openLeagueDetails(activity, leagueId, null, null);
    }

    public static void openLeagueDetails(Activity activity, int leagueId, String leagueName, String leagueIcon) {

        Intent intent = new Intent(activity, LeagueDetailsActivity.class);
        intent.putExtra(LeagueDetailsActivity.ARG_LEAGUE_ID, leagueId);
        intent.putExtra(LeagueDetailsActivity.ARG_LEAGUE_ICON, leagueIcon);
        intent.putExtra(LeagueDetailsActivity.ARG_LEAGUE_NAME, leagueName);
        startActivity(activity, intent);
    }


    public static void openPlayerDetails(Activity activity, int id, String player) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra(BaseActivity.PAGE, BaseActivity.RESULT_LOAD_PLAYER);
        intent.putExtra(BaseActivity.ID, id);
        player = player.replace(" ", "-");
        player = player.replace(".", "");
        intent.putExtra(BaseActivity.PLAYER, player);
        startActivity(activity, intent);
    }

    public static void openTeamDetails(Activity activity, int id) {
        Intent intent = new Intent(activity, TeamDetailsActivity.class);
        intent.putExtra(BaseActivity.PAGE, BaseActivity.RESULT_LOAD_TEAM_PAGE);
        intent.putExtra(BaseActivity.ID, id);
        startActivity(activity, intent);
    }

    public static void openMatchDetails(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, DetailPageActivity.class);
        intent.putExtra(DetailPageActivity.ARG_BUNDLE, bundle);
        startActivity(activity, intent);
    }

    private static void startActivity(Activity activity, Intent intent) {
        activity.startActivityForResult(intent, BaseActivity.REQUEST);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    public static Bitmap getBitmapCountry(int id, Context context) {
        Bitmap bitmap = Cache.getBitmap("Country" + id);
        if (bitmap == null) {
            try {
                Drawable dr;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    dr = context.getResources().getDrawable(id, context.getTheme());
                } else {
                    dr = context.getResources().getDrawable(id);
                }
                Bitmap bmp = Bitmap.createBitmap(200,
                        150, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                dr.draw(canvas);
                bitmap = Utilities.getRoundedCornerBitmap(bmp, Utilities.getPx(context, 0));
                Cache.addBitmap("Country" + id, bitmap);

            } catch (Resources.NotFoundException e) {
                //e.printStackTrace();
            }
        }
        return bitmap;

    }

    public static MaterialDialog showAuthDialog(final Activity ctx) {
        String[] credentials;
        MaterialDialog dialog = new MaterialDialog.Builder(ctx)
                .title(ctx.getString(R.string.loginToAccount))
                .customView(R.layout.login_dialog, true)
                .positiveText(R.string.sign_in)
                .neutralText(R.string.cancel)
                .negativeText(R.string.register)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                    }
                })
                .onPositive(onPositive)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        ctx.startActivity(new Intent(ctx, RegistrationActivity.class));
                    }
                })
                .build();
        return dialog;
    }

    private static MaterialDialog.SingleButtonCallback onPositive = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
            View view = dialog.getCustomView();
            EditText usernameET = (EditText) view.findViewById(R.id.usernameET);
            EditText passwordET = (EditText) view.findViewById(R.id.passwordET);

            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();
            RestClient restClient = buildRetrofit(true);
            final ProgressBar progressBar = (ProgressBar) view.getRootView().findViewById(R.id.progressBar);
            Utilities.showProgressDialog(dialog.getContext(), progressBar);
            Call<BaseAuthRequest.Response> call = restClient.loginRequest(new UserLoginRequest(username, password));
            call.enqueue(new Callback<BaseAuthRequest.Response>() {
                @Override
                public void onResponse(Call<BaseAuthRequest.Response> call, Response<BaseAuthRequest.Response> response) {
                    if (response.body() == null && !response.body().username.isEmpty())
                        Preferences.setUser(response.body().username);
                    else
                        showSnackBar(dialog.getContext(),
                                dialog.getCustomView(),
                                response.body().error);
                    dialog.dismiss();
                    Utilities.dismissProgressDialog(dialog.getContext(), progressBar);
                }

                @Override
                public void onFailure(Call<BaseAuthRequest.Response> call, Throwable t) {
                    showSnackBar(dialog.getContext(), dialog.getCustomView(), t.getMessage());
                    dialog.dismiss();
                    Utilities.dismissProgressDialog(dialog.getContext(), progressBar);
                }
            });


        }
    };

    public interface LoginCallback {
        public void onSuccess();

        public void onError(String error);

    }

    public static void login(Activity ctx) {
        login(ctx, true);
    }

    public static void login(final Activity ctx, final boolean showDialog) {
        login(ctx, showDialog, null);

    }


    public static void login(final Activity ctx, final boolean showDialog, final LoginCallback callback) {
        RestClient rest = buildRetrofit(true);
        final ProgressBar progressBar = (ProgressBar) ctx.findViewById(R.id.progressBar);
        rest.loginRequest(new DeviceLoginRequest()).enqueue(new Callback<BaseAuthRequest.Response>() {
            @Override
            public void onResponse(Call<BaseAuthRequest.Response> call, Response<BaseAuthRequest.Response> response) {
                dismissProgressDialog(ctx, progressBar);
                int x = 0;

                BaseAuthRequest.Response body = response.body();
                if (body.error != null && body.error.equals("UserNotFound")) {
                    Preferences.setUser(null);
                    Preferences.getDefaultPreferences()
                            .edit()
                            .putStringSet(Preferences.PREF_FAVORITES, null)
                            .apply();
                    if (showDialog) {
                        MaterialDialog dialog = showAuthDialog(ctx);
                        dialog.show();
                    }
                    if (callback != null) callback.onError(body.error);
                }
                if (body.username != null && !body.username.isEmpty()) {
                    Preferences.setUser(body.username);
                    Preferences.setIsLogged(true);
                    Preferences.getDefaultPreferences()
                            .edit()
                            .putStringSet(Preferences.PREF_FAVORITES,
                                    new HashSet<String>(body.favorites))
                            .apply();
                    if (callback != null) {
                        callback.onSuccess();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseAuthRequest.Response> call, Throwable t) {
            }
        });
    }

    public static void loadLogoToView(ImageView iv,int teamId){
       loadLogoToView(iv,teamId,false);
    }
    public static void loadLogoToView(ImageView iv,int teamId,boolean transform) {
        String image = Constant.IMAGES_ROOT +
                Constant.LOGO_PATH +
                teamId +
                Constant.LOGO_EXTENSION;
        RequestCreator img = Picasso.with(iv.getContext())
                .load(image);
        if(transform) img.transform(new RoundedCornersTransform(2));
        img.into(iv);
    }

    public static void paintFavoriteIcon(Activity activity, TextView icon, boolean active){
        Typeface tf = FontManager.getTypeface(activity, FontManager.FONTAWESOME);
        icon.setTypeface(tf);
        int color = Color.parseColor("#ffffff");
        String text = activity.getString(R.string.fa_star_o);

        if(active){
            color = ContextCompat.getColor(MyApplication.getAppContext(), R.color.md_white_1000);
            text = activity.getString(R.string.fa_star);

        }
        icon.setText(text);
        icon.setTextColor(color);
    }
}

