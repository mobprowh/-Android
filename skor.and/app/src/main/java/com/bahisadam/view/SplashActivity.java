package com.bahisadam.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bahisadam.R;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.utility.Preferences;
import com.bahisadam.utility.Utilities;

public class SplashActivity extends AppCompatActivity implements Constant {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transparent Status Bar
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_splash);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        if (Utilities.isNetworkAvailable(getApplicationContext())) {
            if(Preferences.isLogged()) Utilities.login(this,false);

            initializeSplash();

        } else {
            Utilities.showSnackBarInternet(this, coordinatorLayout,
                    getString(R.string.no_internet_connection));
        }

    }

    /* Initialize Splash Screen */
    private void initializeSplash() {
        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Utilities.passActivityIntent(SplashActivity.this, HomeActivity.class);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
