package com.epam.safety;

import android.app.Application;

/**
 * Created by dbychkov on 16.04.16.
 */
public class SafetyApplication extends Application {

    private static SafetyApplication singleton;

    private ContactsStorageService contactsStorageService;

    public static SafetyApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        contactsStorageService = new ContactsStorageServiceImpl(this);
    }

    public static ContactsStorageService getContactsStorageService(){
        return getInstance().contactsStorageService;
    }

}
