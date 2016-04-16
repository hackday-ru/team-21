package com.epam.safety;

/**
 * Created by dbychkov on 16.04.16.
 */
public interface SharedPreferencesService {

    ContactsEntity loadContactsFromStorage();

    void saveContactsToStorage(ContactsEntity contactsEntity);

    void saveMessage(String message);

    String getMessage();
}
