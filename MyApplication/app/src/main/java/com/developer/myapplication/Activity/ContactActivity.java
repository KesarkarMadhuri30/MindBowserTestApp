package com.developer.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.developer.myapplication.Adapter.ContactAdapter;
import com.developer.myapplication.Database.DatabaseClient;
import com.developer.myapplication.Database.GetContactFromPhone;
import com.developer.myapplication.Model.ContactModel;
import com.developer.myapplication.R;

import java.util.List;

public class ContactActivity extends AppCompatActivity  {
    RecyclerView contacts_recy;
    LinearLayoutManager layoutManager;
    GetContactFromPhone mydb;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ContactAdapter contactAdapter;

    LinearLayout contact_empty_lyt;
    SearchView searchView;
    boolean table_exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mydb = new GetContactFromPhone(this);

        contact_empty_lyt = findViewById(R.id.contact_empty_lyt);
        contacts_recy= findViewById(R.id.contacts_recy);

        showContacts();

        contacts_recy.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        contacts_recy.setLayoutManager(layoutManager);

        searchView = findViewById(R.id.searchView);
        searchContact();


    }

    private void searchContact() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (contactAdapter!=null)
                    contactAdapter.getFilter().filter(newText);
                return true;

            }
        });
    }


    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {

            setContactDatabase();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setContactDatabase() {
        new ContactLoaderDatabase().execute();
    }


    private class ContactLoaderDatabase extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("###",""+ DatabaseClient.getInstance(ContactActivity.this).getAppDatabase()
                    .contactListDao().isExists());
            table_exist = DatabaseClient.getInstance(ContactActivity.this).getAppDatabase()
                    .contactListDao().isExists();

            if (!table_exist)
            {
                DatabaseClient.getInstance(ContactActivity.this).getAppDatabase()
                        .contactListDao()
                        .addContactData(mydb.getData());
                }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            setRecyclerview();

        }
    }

    private void setRecyclerview() {
        new ContactLoader().execute();
    }


    private class ContactLoader extends AsyncTask<Void,Void, List<ContactModel>> {

        ProgressDialog uploading;
        @Override
        protected List<ContactModel> doInBackground(Void... voids) {
            List<ContactModel> contactAllList = DatabaseClient
                    .getInstance(ContactActivity.this)
                    .getAppDatabase()
                    .contactListDao()
                    .getallContactData(0);
            return contactAllList;
        }

        @Override
        protected void onPreExecute() {
            uploading = ProgressDialog.show(ContactActivity.this, "Loading", "Please wait...", false, false);

        }

        @Override
        protected void onPostExecute(List<ContactModel> contactList) {
            //contactAllList.clear();
           // contactAllList.addAll(contactList);
            Log.d("###","size of list "+contactList.size());
            uploading.dismiss();
            if (contactList.size()!=0){
                contact_empty_lyt.setVisibility(View.GONE);
                contacts_recy.setVisibility(View.VISIBLE);
                contactAdapter = new ContactAdapter(ContactActivity.this, contactList,false);
                contacts_recy.setAdapter(contactAdapter);
            }
            else
            {
                contact_empty_lyt.setVisibility(View.VISIBLE);
                contacts_recy.setVisibility(View.GONE);
            }
        }
    }
}