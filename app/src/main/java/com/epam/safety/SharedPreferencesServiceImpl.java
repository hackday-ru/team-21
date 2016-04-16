package com.epam.safety;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by dbychkov on 16.04.16.
 */
public class SharedPreferencesServiceImpl implements SharedPreferencesService {

    private static final String DEFAULT_MESSAGE = "I am in danger";
    private static final String PREF_CONTACTS_LIST = "contactsList";
    private static final String PREF_MESSAGE = "message";

    private Context context;

    public SharedPreferencesServiceImpl(Context context) {
        this.context = context;
    }

    public ContactsEntity loadContactsFromStorage() {
        String savedValue = getSharedPreferences().getString(PREF_CONTACTS_LIST, "");
        if (savedValue.equals("")) {
            return new ContactsEntity();
        } else {
            return new Gson().fromJson(savedValue, ContactsEntity.class);
        }
    }

    public void saveContactsToStorage(ContactsEntity contactsEntity) {
        getSharedPreferences()
                .edit()
                .putString(PREF_CONTACTS_LIST, new Gson().toJson(contactsEntity))
                .commit();
    }

    public void saveMessage(String message) {
        putString(PREF_MESSAGE, message);
    }

    public String getMessage() {
        return getString(PREF_MESSAGE, DEFAULT_MESSAGE);
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("com.epam.safety", Context.MODE_PRIVATE);
    }

    private void putString(String key, String value) {
        getSharedPreferences()
                .edit()
                .putString(key, value)
                .commit();
    }

    private String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }
}