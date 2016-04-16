package com.epam.safety;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String FIRST_START = "first_start3";

    @Override
    int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    void onCreateLayout(Bundle savedInstanceState) {
        launchIntoActivityIfFirstLaunch();
        initSosButton();
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

    private void initSosButton(){
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsEntity contactsEntity = SafetyApplication.getContactsStorageService().loadContactsFromStorage();
                List<ContactWithPhoneEntity> contacts = contactsEntity.getContactWithPhoneEntityList();
                for (ContactWithPhoneEntity c : contacts){
                    // send message to all contacts
                }
                sendSMSMessage("+79117218627", "Privet");
            }
        });
    }

    protected void sendSMSMessage(String phone, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
