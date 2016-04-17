package com.epam.safety;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dbychkov on 16.04.16.
 */
import android.widget.EditText;
import android.widget.ImageView;

public class IntroSlide extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    public static IntroSlide newInstance(int layoutResId) {
        IntroSlide introSlide = new IntroSlide();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        introSlide.setArguments(args);
        return introSlide;
    }

    private int layoutResId;

    public IntroSlide() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
    }

    private View inflatedView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(layoutResId, container, false);
        // Check whether we are dealing with "enter password" slide or not
        if (inflatedView.findViewById(R.id.password_edit_text) != null){
            initializePasswordSlide();
        }
        return inflatedView;
    }

    /**
     * This config is specific to "enter password"
     */
    private void initializePasswordSlide(){

        final ImageView imageView = (ImageView)inflatedView.findViewById(R.id.password_saved_status);
        final EditText editText = (EditText)inflatedView.findViewById(R.id.password_edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 4) {
                    // Save password
                    SafetyApplication
                            .getSharedPreferencesService()
                            .savePassword(s.toString());
                    // Prevent
                    disableEditText(editText);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

}