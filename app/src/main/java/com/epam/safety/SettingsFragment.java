package com.epam.safety;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Button;
import android.widget.NumberPicker;

import com.epam.safety.eventListeners.CancellationTokenDialogSetButtonListener;
import com.epam.safety.eventListeners.DialogCancelButtonListener;
import com.epam.safety.eventListeners.SosDelayDialogSetButtonListener;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_settings);

        addEventHandlers();
    }

    private void addEventHandlers()
    {
        Preference sosDelayTimePreference = findPreference("sosDelayTime");
        sosDelayTimePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {

                        ShowSosDelayDialog(preference);

                        return true;
                    }

                });

        Preference cancellationPinPreference = findPreference("cancellationPin");
        cancellationPinPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {

                ShowCancellationPinDialog(preference);

                return true;
            }

        });
    }

    private void ShowSosDelayDialog(Preference preference)
    {
        final int minSosDelaySeconds = 1;
        final int maxSosDelaySeconds = 15;

        Dialog dialog = new Dialog(preference.getContext());
        dialog.setTitle("Таймер отмены (в секундах)");
        dialog.setContentView(R.layout.dialog_sos_delay);

        NumberPicker delayPicker = (NumberPicker) dialog.findViewById(R.id.delay_picker);
        delayPicker.setMinValue(minSosDelaySeconds);
        delayPicker.setMaxValue(maxSosDelaySeconds);
        delayPicker.setWrapSelectorWheel(true);

        // TODO set delayPicker value here

        Button setButton = (Button) dialog.findViewById(R.id.set_button);
        SosDelayDialogSetButtonListener sosDelayDialogSetButtonListener = new SosDelayDialogSetButtonListener();
        sosDelayDialogSetButtonListener.preference = preference;
        sosDelayDialogSetButtonListener.dialog = dialog;
        setButton.setOnClickListener(sosDelayDialogSetButtonListener);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        DialogCancelButtonListener cancelButtonOnClickListener = new DialogCancelButtonListener();
        cancelButtonOnClickListener.dialog = dialog;
        cancelButton.setOnClickListener(cancelButtonOnClickListener);

        dialog.show();
    }

    private void ShowCancellationPinDialog(Preference preference)
    {
        Dialog dialog = new Dialog(preference.getContext());
        dialog.setTitle("PIN для отмены");
        dialog.setContentView(R.layout.dialog_cancellation_token);

        Button setButton = (Button) dialog.findViewById(R.id.set_button);
        CancellationTokenDialogSetButtonListener cancellationTokenDialogSetButtonListener = new CancellationTokenDialogSetButtonListener();
        cancellationTokenDialogSetButtonListener.dialog = dialog;
        setButton.setOnClickListener(cancellationTokenDialogSetButtonListener);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        DialogCancelButtonListener cancelButtonOnClickListener = new DialogCancelButtonListener();
        cancelButtonOnClickListener.dialog = dialog;
        cancelButton.setOnClickListener(cancelButtonOnClickListener);

        dialog.show();
    }
}
