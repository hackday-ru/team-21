package com.epam.safety;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditMessageActivity extends BaseActivity {

    private EditText editText;

    @Override
    int getLayoutResource() {
        return R.layout.activity_edit_message;
    }

    @Override
    void onCreateLayout(Bundle savedInstanceState) {
        initViews();
        setToolbarText("Edit message");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //setEditMessageSelected();
    }

    private void initViews() {
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(SafetyApplication.getContactsStorageService().getMessage());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyApplication.getContactsStorageService().saveMessage(editText.getText().toString());
                Toast.makeText(EditMessageActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
