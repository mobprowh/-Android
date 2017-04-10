package com.bahisadam.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.bahisadam.Listeners.OnFooterItemClickListener;
import com.bahisadam.R;
import com.bahisadam.adapter.FooterToolbarAdapter;
import com.bahisadam.interfaces.Constant;
import com.bahisadam.model.ToolbarItem;
import com.bahisadam.utility.Preferences;
import com.bahisadam.utility.Utilities;

import java.util.List;

/**
 * Created by atata on 20/12/2016.
 * BaseActivity
 */

public class BaseActivity extends AppCompatActivity {
    protected LinearLayout toolbar_bottom;
    private FooterToolbarAdapter adapter;
    private Menu mMenu;
    public static final String PAGE = "PAGE";
    public static final String ID = "ID";
    public static final String PLAYER = "PLAYER";

    public static final int RESULT_LOAD_HOME_PAGE = 1012,
            RESULT_LOAD_FAVORITE = 1013,
            RESULT_LOAD_LIVE = 1014,
            RESULT_LOAD_TOURNAMENTS = 1015,
            RESULT_LOAD_TEAM_PAGE = 1016,
            RESULT_LOAD_PLAYER=1017;
    public static final int REQUEST = 1011;


    public void loadHomePage(){
        animateStartActivity(RESULT_LOAD_HOME_PAGE);

    }
    public void loadFavorite(){
        animateStartActivity(RESULT_LOAD_FAVORITE);


    }
    public void loadLive(){
        animateStartActivity(RESULT_LOAD_LIVE);

    }

    public void loadTournaments(){

        animateStartActivity(RESULT_LOAD_TOURNAMENTS);
    }

    private void animateStartActivity(int extra){
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra(PAGE,extra);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        updateUi();

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        /*
        if (id == R.id.action_logout) {
            Preferences.setIsLogged(false);
            updateUi();
            return true;
        }
        if (id == R.id.action_login) {
            Utilities.login(this,true);
            updateUi();

            return true;
        }*/
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUi(){
        //MenuItem loginItem = mMenu.findItem(R.id.action_login);
        //loginItem.setVisible(Preferences.getUser() == null);
        //MenuItem logoutItem = mMenu.findItem(R.id.action_logout);
        //logoutItem.setVisible(Preferences.getUser() != null);
    }
    public void initFooterToolbar(){


        toolbar_bottom = (LinearLayout) findViewById(R.id.toolbar_bottom);

        List<ToolbarItem> toolbarItems = Utilities.createToolbarItems(this);
        adapter = new FooterToolbarAdapter(this,toolbarItems);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.toolbarRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnFooterItemClickListener(this));
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
    }

    public void setActiveToolbarItem(int num){
        adapter.setActive(num);
    }
    public void onFooterItemSelected(int num){
        switch (num) {
            case 0:
                loadHomePage();
                break;
            case 1:
                loadTournaments();

                break;
            case 2:


                loadLive();
                break;

            case 3:
                loadFavorite();
                break;

            case 4:
                Utilities.openLeagueDetails(this,
                        Constant.TURKEY_SUPER_LEAGUE,
                        getString(R.string.TurkeySuperLeague),
                        getString(R.string.superLeagueFlag));
                break;

            default:
                break;
        }



    }

}
