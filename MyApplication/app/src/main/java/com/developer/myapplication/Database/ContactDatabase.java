package com.developer.myapplication.Database;

import com.developer.myapplication.Model.ContactModel;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities={ContactModel.class},version = 1)
public abstract class ContactDatabase extends RoomDatabase {

    public abstract ContactListDao contactListDao();
}
