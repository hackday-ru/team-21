package com.epam.safety;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactView extends FrameLayout {

    private View inflatedView;
    private TextView contactName;
    private TextView contactNumber;
    private ImageView removeButton;
    private ContactWithPhoneEntity entity;

    public ContactView(Context context) {
        super(context);
        setupViews();
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupViews();
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedView = inflater.inflate(R.layout.view_contact, this, true);
        contactName = (TextView)inflatedView.findViewById(R.id.contact_name);
        contactNumber = (TextView)inflatedView.findViewById(R.id.contact_number);
        removeButton = (ImageView)inflatedView.findViewById(R.id.remove_button);
    }

    public void setData(ContactWithPhoneEntity entity){
        this.entity = entity;
        renderData();
    }

    public void setOnRemoveListener(OnClickListener listener){
        removeButton.setOnClickListener(listener);
    }

    private void renderData(){
        contactName.setText(entity.getContactName());
        contactNumber.setText(entity.getContactNumber());
    }
}