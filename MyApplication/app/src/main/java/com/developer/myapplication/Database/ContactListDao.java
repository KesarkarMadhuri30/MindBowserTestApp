package com.developer.myapplication.Database;

import com.developer.myapplication.Model.ContactModel;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ContactListDao {
    @Query("SELECT EXISTS(SELECT * FROM allcontactlist)")
    public Boolean isExists();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addContactData(List<ContactModel> userList);

    @Query("select * from allcontactlist WHERE delete_status=:delete_status")
    List<ContactModel> getallContactData(int delete_status);

    @Query("UPDATE allcontactlist SET fav_status=:status WHERE auto_id = :id")
    void updatefavStatus(int status, int id);

    @Query("SELECT * FROM allcontactlist WHERE fav_status=:fav_status AND delete_status=:del_status")
    List<ContactModel> getFavData(int fav_status, int del_status);

    @Query("UPDATE allcontactlist SET delete_status=:status WHERE auto_id = :id")
    void update_deleteStatus(int status, int id);


}
