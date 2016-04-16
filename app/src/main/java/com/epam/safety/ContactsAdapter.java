package com.epam.safety;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dbychkov on 16.04.16.
 */
public class ContactsAdapter extends BaseListAdapter<ContactWithPhoneEntity, ContactView> implements View.OnClickListener{

    private View.OnClickListener onClickListener;

    public ContactsAdapter(Context context,  View.OnClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;
    }

    @Override
    protected ContactView createView(Context context, ViewGroup viewGroup, int viewType) {
        return (ContactView) LayoutInflater.from(context)
                .inflate(R.layout.adapter_contact, viewGroup, false);
    }

    @Override
    protected void bind(final ContactWithPhoneEntity contact, ContactView view, final ViewHolder<ContactView> holder) {
        view.setData(contact);
        view.setTag(contact);
        view.setOnRemoveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder.getAdapterPosition());
            }
        });
    }



    public void setOnCLickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void onClick(View view) {
        onClickListener.onClick(view);
    }
}