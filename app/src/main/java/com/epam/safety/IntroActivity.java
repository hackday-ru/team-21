package com.epam.safety;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;


public final class IntroActivity  extends AppIntro2 {

    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(AppIntroFragment.newInstance("Slide 1", "Description 1", R.drawable.ic_slide1, Color.parseColor("#222222")));
        addSlide(AppIntroFragment.newInstance("Slide 2", "Description 2", R.drawable.ic_slide2, Color.parseColor("#00BCD4")));
        addSlide(AppIntroFragment.newInstance("Slide 3", "Description 3", R.drawable.ic_slide3, Color.parseColor("#5C6BC0")));
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

}