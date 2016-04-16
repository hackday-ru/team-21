package com.epam.safety;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dbychkov on 16.04.16.
 */
public class ContactWithPhoneEntity implements Parcelable {

    String contactName;
    String contactNumber;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public ContactWithPhoneEntity() {
    }

    public ContactWithPhoneEntity(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contactName);
        dest.writeString(this.contactNumber);
    }


    protected ContactWithPhoneEntity(Parcel in) {
        this.contactName = in.readString();
        this.contactNumber = in.readString();
    }

    public static final Creator<ContactWithPhoneEntity> CREATOR = new Creator<ContactWithPhoneEntity>() {
        @Override
        public ContactWithPhoneEntity createFromParcel(Parcel source) {
            return new ContactWithPhoneEntity(source);
        }

        @Override
        public ContactWithPhoneEntity[] newArray(int size) {
            return new ContactWithPhoneEntity[size];
        }
    };
}
