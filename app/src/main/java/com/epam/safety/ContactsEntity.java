package com.epam.safety;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbychkov on 16.04.16.
 */
public class ContactsEntity implements Parcelable {
    private List<ContactWithPhoneEntity> contactWithPhoneEntityList = new ArrayList<>();

    public List<ContactWithPhoneEntity> getContactWithPhoneEntityList() {
        return contactWithPhoneEntityList;
    }

    public void setContactWithPhoneEntityList(List<ContactWithPhoneEntity> contactWithPhoneEntityList) {
        this.contactWithPhoneEntityList = contactWithPhoneEntityList;
    }

    public void addContactWithPhoneEntityList(ContactWithPhoneEntity entity){
        contactWithPhoneEntityList.add(entity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.contactWithPhoneEntityList);
    }

    public ContactsEntity() {
    }

    protected ContactsEntity(Parcel in) {
        this.contactWithPhoneEntityList = new ArrayList<ContactWithPhoneEntity>();
        in.readList(this.contactWithPhoneEntityList, ContactWithPhoneEntity.class.getClassLoader());
    }

    public static final Creator<ContactsEntity> CREATOR = new Creator<ContactsEntity>() {
        @Override
        public ContactsEntity createFromParcel(Parcel source) {
            return new ContactsEntity(source);
        }

        @Override
        public ContactsEntity[] newArray(int size) {
            return new ContactsEntity[size];
        }
    };
}
