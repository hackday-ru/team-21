package com.epam.safety;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

public class EditContactsActivity extends BaseActivity {

    private static final int PICK_CONTACT = 1;
    private static final String PREF_CONTACTS_LIST = "contactsList";

    @Override
    int getLayoutResource() {
        return R.layout.activity_edit_contacts;
    }

    @Override
    void onCreateLayout(Bundle savedInstanceState) {
        setupFab();
        initRecyclerView();
        loadContactsFromStorage();

    }

    public void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(EditContactsActivity.this, "Contacts", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

    }

    private RecyclerViewWithEmptyView recyclerView;
    private ContactsAdapter contactsAdapter;

    private void initRecyclerView() {
        recyclerView = (RecyclerViewWithEmptyView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactsAdapter = new ContactsAdapter(this));
        NestedScrollView nestedScrollView= (NestedScrollView)findViewById(R.id.scroll);
        recyclerView.setEmptyView(nestedScrollView);
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        String phoneNumber = "";
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            phoneNumber = phones.getString(phones.getColumnIndex("data1"));
                        }
                        String contactName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        contactsEntity.addContactWithPhoneEntityList(new ContactWithPhoneEntity(contactName, phoneNumber));
                    }
                }
                break;
        }
        saveContactsToStorage();
        reinitAdapter();
    }

    private ContactsEntity contactsEntity;

    private void loadContactsFromStorage() {
        String savedValue = PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_CONTACTS_LIST, "");
        if (savedValue.equals("")) {
            contactsEntity = new ContactsEntity();
        } else {
            contactsEntity = new Gson().fromJson(savedValue, ContactsEntity.class);
        }
        reinitAdapter();
    }

    public void reinitAdapter() {
        contactsAdapter = new ContactsAdapter(this);
        contactsAdapter.clearItems();
        contactsAdapter.setItems(contactsEntity.getContactWithPhoneEntityList());
        recyclerView.setAdapter(contactsAdapter);
    }


    public void saveContactsToStorage() {
        PreferenceManager
                .getDefaultSharedPreferences(this)
                .edit()
                .putString(PREF_CONTACTS_LIST, new Gson().toJson(contactsEntity))
                .commit();


    }
}
