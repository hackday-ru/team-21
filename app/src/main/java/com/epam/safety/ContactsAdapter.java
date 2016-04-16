package com.epam.safety;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dbychkov on 16.04.16.
 */
public class ContactsAdapter extends BaseListAdapter<ContactWithPhoneEntity, ContactView> {

    private ItemRemovedListener itemRemovedListener;

    public ContactsAdapter(Context context, ItemRemovedListener itemRemovedListener) {
        super(context);
        this.itemRemovedListener = itemRemovedListener;
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
                itemRemovedListener.remove();
            }
        });
    }

    public interface ItemRemovedListener {
        void remove();
    }
}