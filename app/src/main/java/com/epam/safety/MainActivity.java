package com.epam.safety;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends BaseActivity {

    private static final String FIRST_START = "first_start3";

    @Override
    int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    void onCreateLayout(Bundle savedInstanceState) {
        launchIntoActivityIfFirstLaunch();
    }

    private void launchIntoActivityIfFirstLaunch(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                boolean isFirstStart = getPrefs.getBoolean(FIRST_START, true);
                if (isFirstStart) {
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putBoolean(FIRST_START, false);
                    e.apply();
                }
            }
        });
        t.start();
    }


}
