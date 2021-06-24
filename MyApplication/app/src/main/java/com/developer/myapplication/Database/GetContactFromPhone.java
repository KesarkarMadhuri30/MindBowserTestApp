package com.developer.myapplication.Database;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.developer.myapplication.Model.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class GetContactFromPhone {
    Context context;
    Cursor mCursor;

    public GetContactFromPhone(Context context) {
        this.context = context;
    }


    public List<ContactModel> getData()
    {
        List<ContactModel> data = new ArrayList<>();

        mCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (mCursor !=null)
        {
            Log.e("count", "" + mCursor.getCount());
            if (mCursor.getCount() == 0)
            {
                Toast.makeText(context, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
            }

            while (mCursor.moveToNext())
            {
                String id = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone_no = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contact_img = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                int fav_status = 0;
                int delete_status = 0;

               data.add(new ContactModel(id,name,phone_no,contact_img,fav_status,delete_status));

            }
        }
        else {
            Log.e("Cursor close 1", "----");
        }

        mCursor.close();
        return data;
    }
}
