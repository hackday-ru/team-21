package com.epam.safety;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity {

    private static final String FIRST_START = "first_start3";

    private static final int DURATION_MILLIS = 10_000;

    private TextView timerText;
    private CounterClass timer;
    private Animation animationFadeIn;
    private Animation animationFadeOut;
    private FrameLayout timerContainer;

    @Override
    int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    void onCreateLayout(Bundle savedInstanceState) {
        launchIntoActivityIfFirstLaunch();
        initTimerContainer();
        initTimer();
        initSosButton();
        initCancelButton();
        initAnimations();

    }

    private void initTimerContainer(){
        timerContainer = (FrameLayout) findViewById(R.id.time_container);
    }

    private void initAnimations() {
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
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

    private void initTimer() {
        timerText = (TextView) findViewById(R.id.timer_text);
        timer = new CounterClass(DURATION_MILLIS, 1000);
    }

    private void initSosButton() {
        findViewById(R.id.button_sos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerContainer.startAnimation(animationFadeIn);
                timerContainer.setVisibility(View.VISIBLE);
                timer.start();
            }
        });
    }

    private void initCancelButton() {
        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                timerContainer.startAnimation(animationFadeOut);
                timerContainer.setVisibility(View.INVISIBLE);
            }
        });
    }

    private int sendMessagesToAllRecipients() {
        ContactsEntity contactsEntity = SafetyApplication.getContactsStorageService().loadContactsFromStorage();
        List<ContactWithPhoneEntity> contacts = contactsEntity.getContactWithPhoneEntityList();
        for (ContactWithPhoneEntity c : contacts) {
            sendSMSMessage(c.getContactNumber(), SafetyApplication.getContactsStorageService().getMessage());
        }
        return contacts.size();
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

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            int count = sendMessagesToAllRecipients();
            timerContainer.startAnimation(animationFadeOut);
            timerContainer.setVisibility(View.INVISIBLE);
            String msg = String.format("Message sent to %s recipients", count);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            timerText.setText(hms);
        }
    }


}
