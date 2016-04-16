package com.epam.safety;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

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

    private void launchIntoActivityIfFirstLaunch() {
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

    private void initSosButton() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsEntity contactsEntity = SafetyApplication.getContactsStorageService().loadContactsFromStorage();
                List<ContactWithPhoneEntity> contacts = contactsEntity.getContactWithPhoneEntityList();
                for (ContactWithPhoneEntity c : contacts) {
                    sendSMSMessage(c.getContactNumber(), SafetyApplication.getContactsStorageService().getMessage());
                }

            }
        });
    }

    protected void sendSMSMessage(String phone, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, concatMessageWithLocation(message, getLastKnownLocationAsString()), null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private String concatMessageWithLocation(String message, String location) {
        return message + "\n" + location;
    }

    private String getLastKnownLocationAsString() {
        Location location = getLastKnownLocation();
        if (location == null) {
            return "";
        }
        return String.format("http://maps.google.com/maps?q=%s,%s", location.getLatitude(), location.getLongitude());
    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

}
