package com.epam.safety.eventListeners;

import android.app.Dialog;
import android.view.View;

public class DialogCancelButtonListener implements View.OnClickListener {

    public Dialog dialog;

    @Override
    public void onClick(View view) {
        dialog.dismiss();
    }
}
