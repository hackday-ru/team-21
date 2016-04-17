package com.epam.safety;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.blurlockview.BlurLockView;
import com.nightonke.blurlockview.Password;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity implements BlurLockView.OnPasswordInputListener,
        BlurLockView.OnLeftButtonClickListener{

    private static final String FIRST_START = "first_start13";

    private static final int DURATION_MILLIS = 10_000;

    private TextView timerText;
    private CounterClass timer;
    private Animation animationFadeIn;
    private Animation animationFadeOut;

    private boolean stateRunning = false;
    private Button sosButton;

    @Override
    int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    void onCreateLayout(Bundle savedInstanceState) {
        launchIntoActivityIfFirstLaunch();
        initTimer();
        initSosButton();
        initAnimations();
        initBlurImageView();
    }

    private BlurLockView blurLockView;

    private void initBlurImageView(){
        blurLockView = (BlurLockView)findViewById(R.id.blurlockview);
        blurLockView.setBlurredView(findViewById(R.id.background_image));
        blurLockView.setCorrectPassword(SafetyApplication.getSharedPreferencesService().getPassword());
        blurLockView.setTitle("Title");
        blurLockView.setLeftButton("Cancel");
        blurLockView.setRightButton("");
        blurLockView.setType(getPasswordType(), false);
        blurLockView.setOnLeftButtonClickListener(this);
        blurLockView.setOnPasswordInputListener(this);
        hidePinPad();
    }

    private Password getPasswordType() {
        if ("PASSWORD_NUMBER".equals(getIntent().getStringExtra("PASSWORD_TYPE")))
            return Password.NUMBER;
        else if ("PASSWORD_NUMBER".equals(getIntent().getStringExtra("PASSWORD_TYPE")))
            return Password.TEXT;
        return Password.NUMBER;
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

    private void showPinPad(){
        blurLockView.show(
                getIntent().getIntExtra("SHOW_DURATION", 0),
                Utils.getShowType(getIntent().getIntExtra("SHOW_DIRECTION", 0)),
                Utils.getEaseType(getIntent().getIntExtra("SHOW_EASE_TYPE", 30)));
        blurLockView.bringToFront();
        sosButton.setVisibility(View.INVISIBLE);
    }

    private void initSosButton() {
        sosButton = (Button)findViewById(R.id.button_sos);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!stateRunning) {

                    timerText.startAnimation(animationFadeIn);
                    timerText.setVisibility(View.VISIBLE);
                    timer.start();
                    stateRunning = true;
                    sosButton.setText("X");
                } else {
                    showPinPad();
                }
            }
        });
    }

    private int sendMessagesToAllRecipients() {
        ContactsEntity contactsEntity = SafetyApplication.getSharedPreferencesService().loadContactsFromStorage();
        List<ContactWithPhoneEntity> contacts = contactsEntity.getContactWithPhoneEntityList();
        for (ContactWithPhoneEntity c : contacts) {
            //sendSMSMessage(c.getContactNumber(), SafetyApplication.getSharedPreferencesService().getMessage());
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

    @Override
    public void onClick() {
        hidePinPad();
    }


    @Override
    public void correct(String inputPassword) {
        hidePinPad();
        timer.cancel();
        timerText.setVisibility(View.INVISIBLE);
        stateRunning = false;
        sosButton.setText("SOS");
    }

    private void hidePinPad(){
        blurLockView.hide(
                getIntent().getIntExtra("HIDE_DURATION", 0),
                Utils.getHideType(getIntent().getIntExtra("HIDE_DIRECTION", 0)),
                Utils.getEaseType(getIntent().getIntExtra("HIDE_EASE_TYPE", 30)));
        sosButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void incorrect(String inputPassword) {
        Toast.makeText(this,
                "Incorrect",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void input(String inputPassword) {
    }

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            int count = sendMessagesToAllRecipients();
            timerText.startAnimation(animationFadeOut);
            timerText.setVisibility(View.INVISIBLE);
            String msg = String.format("Message sent to %s recipients", count);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            stateRunning = false;
            sosButton.setText("SOS");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            timerText.setText(hms);
            blurLockView.setTitle(hms);
        }
    }


}
