package com.developer.myapplication.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.developer.myapplication.R;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cv_contact,cv_delete,cv_fav;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cv_contact= findViewById(R.id.cv_contact);
        cv_contact.setOnClickListener(this);

        cv_delete = findViewById(R.id.cv_delete);
        cv_delete.setOnClickListener(this);

        cv_fav =findViewById(R.id.cv_fav);
        cv_fav.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        String alertmsg = "Do you really want to Exit?";
        ExitAlertdialog(alertmsg);
    }

    private void ExitAlertdialog(String alertmsg) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.delete_alert_dialog, null);
        final TextView txt_dialog = alertLayout.findViewById(R.id.txt_dialog);
        final TextView btn_yes = alertLayout.findViewById(R.id.btn_yes);
        final TextView btn_no = alertLayout.findViewById(R.id.btn_no);
        btn_yes.setText("Exit");
        btn_no.setText("Cancel");


        txt_dialog.setText("" + alertmsg);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);


        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        dialog.setCancelable(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finishAffinity();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cv_contact:
                Intent contact =new Intent(this, ContactActivity.class);
                startActivity(contact);
                break;
            case R.id.cv_fav:
                Intent fav =new Intent(this, FavoriteActivity.class);
                startActivity(fav);
                break;
            case R.id.cv_delete:
                Intent delete =new Intent(this, DeletedContactActivity.class);
                startActivity(delete);
                break;





        }
    }
}