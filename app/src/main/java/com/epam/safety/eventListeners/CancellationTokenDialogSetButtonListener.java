package com.epam.safety.eventListeners;

import android.app.Dialog;
import android.view.View;

public class CancellationTokenDialogSetButtonListener implements View.OnClickListener {

    public Dialog dialog;

    @Override
    public void onClick(View v) {

        // TODO validate value of pins and save new pin here

        dialog.dismiss();
    }

}
