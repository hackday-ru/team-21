package com.epam.safety;

/**
 * Created by dbychkov on 16.04.16.
 */
public interface ContactsStorageService {

    ContactsEntity loadContactsFromStorage();

    void saveContactsToStorage(ContactsEntity contactsEntity);
}
