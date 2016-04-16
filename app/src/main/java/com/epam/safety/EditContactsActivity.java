package com.epam.safety;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

public class EditContactsActivity extends BaseActivity {


    @Override
    int getLayoutResource() {
        return R.layout.activity_edit_contacts;
    }

    @Override
    void onCreateLayout(Bundle savedInstanceState) {
        setupFab();
    }

    public void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditContactsActivity.this, "Contacts", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
