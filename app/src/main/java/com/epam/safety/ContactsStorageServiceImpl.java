package com.epam.safety;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by dbychkov on 16.04.16.
 */
public class ContactsStorageServiceImpl implements ContactsStorageService{

    private static final String PREF_CONTACTS_LIST = "contactsList";

    private Context context;

    public ContactsStorageServiceImpl(Context context){
        this.context = context;
    }

    public ContactsEntity loadContactsFromStorage() {
        String savedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_CONTACTS_LIST, "");
        if (savedValue.equals("")) {
            return new ContactsEntity();
        } else {
            return new Gson().fromJson(savedValue, ContactsEntity.class);
        }
    }

    public void saveContactsToStorage(ContactsEntity contactsEntity) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CONTACTS_LIST, new Gson().toJson(contactsEntity))
                .commit();


    }
}
