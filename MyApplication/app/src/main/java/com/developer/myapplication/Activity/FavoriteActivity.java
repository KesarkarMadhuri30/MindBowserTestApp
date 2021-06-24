package com.developer.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import com.developer.myapplication.Adapter.FavoriteAdapter;
import com.developer.myapplication.Database.DatabaseClient;
import com.developer.myapplication.Model.ContactModel;
import com.developer.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView favorite_cont_recy;
    LinearLayoutManager layoutManager;
    List<ContactModel> favContactList = new ArrayList<>();
    FavoriteAdapter favoriteAdapter;
    LinearLayout favorite_empty_lyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);



        favorite_empty_lyt = findViewById(R.id.favorite_empty_lyt);

        favorite_cont_recy= findViewById(R.id.favorite_cont_recy);
        favorite_cont_recy.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        favorite_cont_recy.setLayoutManager(layoutManager);
        setRecyclerview();
    }

    private void setRecyclerview() {
        favContactList.clear();
        new FavoriteContactLoader().execute();
    }


    private class FavoriteContactLoader extends AsyncTask<Void,Void, List<ContactModel>> {

        ProgressDialog uploading;
        @Override
        protected List<ContactModel> doInBackground(Void... voids) {
            favContactList = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .contactListDao()
                    .getFavData(1,0);
            return favContactList;
        }

        @Override
        protected void onPreExecute() {

            uploading = ProgressDialog.show(FavoriteActivity.this, "Loading", "Please wait...", false, false);

        }

        @Override
        protected void onPostExecute(List<ContactModel> favList) {
            uploading.dismiss();
            if (favList.size()!=0){
                favorite_cont_recy.setVisibility(View.VISIBLE);
                favorite_empty_lyt.setVisibility(View.GONE);
                favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this, favList);
                favorite_cont_recy.setAdapter(favoriteAdapter);
                favoriteAdapter.notifyDataSetChanged();
            }
            else
            {
                favorite_cont_recy.setVisibility(View.GONE);
                favorite_empty_lyt.setVisibility(View.VISIBLE);
                //Toast.makeText(FavoriteActivity.this, ""+favList.size(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}