package com.epam.safety;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

public class EditContactsActivity extends BaseActivity implements ContactsAdapter.ItemRemovedListener {

    private static final int PICK_CONTACT = 1;

    private RecyclerViewWithEmptyView recyclerView;
    private ContactsAdapter contactsAdapter;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_edit_contacts;
    }

    @Override
    void onCreateLayout(Bundle savedInstanceState) {
        initToolbar();
        setupFab();
        initRecyclerView();
        loadContactsFromStorage();
        setToolbarText("Edit contacts");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //setEditContactsSelected();
    }

    private void initToolbar(){
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbarLayout.setTitle("Contacts");
    }

    public void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start contacts picker activity
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerViewWithEmptyView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactsAdapter = new ContactsAdapter(this, this));
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.scroll);
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

                        // Add selected contact to the list
                        contactsAdapter.addItem(new ContactWithPhoneEntity(contactName, phoneNumber));
                        // Persist this changes
                        saveContactsToStorage();
                    }
                }
                break;
        }
    }

    /**
     * Initialize list adapter with data from persistent storage
     */
    private void loadContactsFromStorage() {
        contactsAdapter.setItems(
                SafetyApplication
                        .getSharedPreferencesService()
                        .loadContactsFromStorage()
                        .getContactWithPhoneEntityList()
        );
    }

    /**
     * Save contacts from adapter to persistent storage
     */
    public void saveContactsToStorage() {
        ContactsEntity contactsEntity = new ContactsEntity();
        contactsEntity.setContactWithPhoneEntityList(contactsAdapter.getItems());
        SafetyApplication
                .getSharedPreferencesService()
                .saveContactsToStorage(contactsEntity);
    }

    /**
     * Contact was removed from list, persist this changes to storage
     */
    @Override
    public void remove() {
        saveContactsToStorage();
    }
}
