package com.developer.myapplication.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="allcontactlist")
public class ContactModel {
    @PrimaryKey(autoGenerate = true)
    int auto_id;

    @ColumnInfo(name = "contact_id")
    String contact_id;

    @ColumnInfo(name = "contactname")
    String contactname;

    @ColumnInfo(name = "contactphone")
    String contactphone;

    @ColumnInfo(name = "imagepath")
    String imagepath;

    @ColumnInfo(name = "fav_status")
    int fav_status;

    @ColumnInfo(name = "delete_status")
    int delete_status;

    public ContactModel(String contact_id, String contactname, String contactphone, String imagepath, int fav_status, int delete_status) {
        this.contact_id = contact_id;
        this.contactname = contactname;
        this.contactphone = contactphone;
        this.imagepath = imagepath;
        this.fav_status = fav_status;
        this.delete_status = delete_status;
    }

    public int getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(int auto_id) {
        this.auto_id = auto_id;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public int getFav_status() {
        return fav_status;
    }

    public void setFav_status(int fav_status) {
        this.fav_status = fav_status;
    }

    public int getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(int delete_status) {
        this.delete_status = delete_status;
    }
}

