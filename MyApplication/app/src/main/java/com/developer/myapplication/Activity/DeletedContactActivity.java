package com.developer.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.developer.myapplication.Adapter.DeleteAdapter;
import com.developer.myapplication.Database.DatabaseClient;
import com.developer.myapplication.Model.ContactModel;
import com.developer.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DeletedContactActivity extends AppCompatActivity {
    RecyclerView delete_cont_recy;
    LinearLayoutManager layoutManager;
    List<ContactModel> delContactList = new ArrayList<>();
    DeleteAdapter deleteAdapter;
    LinearLayout delete_empty_lyt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_contact);


        delete_empty_lyt = findViewById(R.id.delete_empty_lyt);

        delete_cont_recy = findViewById(R.id.delete_cont_recy);
        delete_cont_recy.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        delete_cont_recy.setLayoutManager(layoutManager);
        setRecyclerview();
    }

    private void setRecyclerview() {
        delContactList.clear();
        new DeleteContactLoader().execute();
    }



    private class DeleteContactLoader extends AsyncTask<Void,Void, List<ContactModel>> {

        ProgressDialog uploading;
        @Override
        protected List<ContactModel> doInBackground(Void... voids) {
            delContactList = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .contactListDao()
                    .getallContactData(1);
            return delContactList;
        }

        @Override
        protected void onPreExecute() {

            uploading = ProgressDialog.show(DeletedContactActivity.this, "Loading", "Please wait...", false, false);

        }

        @Override
        protected void onPostExecute(List<ContactModel> deleteList) {
            uploading.dismiss();
            if (deleteList.size()!=0){
                delete_cont_recy.setVisibility(View.VISIBLE);
                delete_empty_lyt.setVisibility(View.GONE);
                deleteAdapter = new DeleteAdapter(DeletedContactActivity.this, deleteList);
                delete_cont_recy.setAdapter(deleteAdapter);
                deleteAdapter.notifyDataSetChanged();
            }
            else
            {
                delete_cont_recy.setVisibility(View.GONE);
                delete_empty_lyt.setVisibility(View.VISIBLE);
               // Toast.makeText(DeletedContactActivity.this, ""+deleteList.size(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}