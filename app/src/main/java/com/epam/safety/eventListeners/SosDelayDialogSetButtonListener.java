package com.epam.safety.eventListeners;

import android.app.Dialog;
import android.preference.Preference;
import android.view.View;
import android.widget.NumberPicker;

import com.epam.safety.R;
import com.epam.safety.SafetyApplication;

public class SosDelayDialogSetButtonListener implements View.OnClickListener {

    public Preference preference;
    public Dialog dialog;

    @Override
    public void onClick(View view) {

        NumberPicker delayPicker = (NumberPicker) dialog.findViewById(R.id.delay_picker);
        int pickedDelay = delayPicker.getValue();

        // TODO save delay here

        String units = " seconds";
        if (pickedDelay == 1)
        {
            units = " second";
        }

        preference.setSummary(pickedDelay + units);


        SafetyApplication.getSharedPreferencesService().saveDelay(pickedDelay);

        dialog.dismiss();
    }
}
