package com.developer.myapplication.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.myapplication.Database.DatabaseClient;
import com.developer.myapplication.Model.ContactModel;
import com.developer.myapplication.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Viewholder> {
    Activity context;
    List<ContactModel> mFavContactList;
    String strFirstletter;


    public FavoriteAdapter(Activity context, List<ContactModel> mFavContactList) {
        this.context = context;
        this.mFavContactList = mFavContactList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.fav_cont_listitem,parent,false);
        return new Viewholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String imagepath = mFavContactList.get(position).getImagepath();
        if (imagepath != null) {
            holder.fav_profile_pic.setVisibility(View.VISIBLE);
            holder.fav_first_ltter.setVisibility(View.GONE);
            Glide.with(context).load(imagepath).into(holder.fav_profile_pic);
        }else {
            holder.fav_profile_pic.setVisibility(View.GONE);
            holder.fav_first_ltter.setVisibility(View.VISIBLE);
            strFirstletter = mFavContactList.get(position).getContactname();
            strFirstletter = strFirstletter.substring(0,1);
            holder.fav_first_ltter.setText(""+strFirstletter);
        }
        holder.fav_contact_name.setText(mFavContactList.get(position).getContactname());
        holder.fav_contact_no.setText(mFavContactList.get(position).getContactphone());

        if (mFavContactList.get(position).getFav_status()==1)
        {
            holder.fav_fav_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
        }


        holder.fav_fav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class DeleteTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        DatabaseClient.getInstance(context).getAppDatabase()
                                .contactListDao()
                                .updatefavStatus(0,mFavContactList.get(position).getAuto_id());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        notifyDataSetChanged();
                        mFavContactList.remove(position);
                        Toast.makeText(context, "Contact Removed from favorite", Toast.LENGTH_LONG).show();
                    }
                }

                DeleteTask dt = new DeleteTask();
                dt.execute();
            }
        });


        holder.fav_contact_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = holder.fav_contact_no.getText().toString();

                setChoiceDialog(phoneNumber);
            }
        });
    }

    private void setChoiceDialog(String phoneNumber) {
        LayoutInflater inflater = context.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.choice_dialog, null);

        final CardView card_call = alertLayout.findViewById(R.id.card_call);
        final CardView card_message = alertLayout.findViewById(R.id.card_message);


        final AlertDialog.Builder alert = new AlertDialog.Builder(context);


        alert.setView(alertLayout);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        dialog.setCancelable(true);

        card_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(i);
            }
        });

        card_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                context.startActivity(intent);
            }
        });
        dialog.show();

    }


    @Override
    public int getItemCount() {
        return mFavContactList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView fav_profile_pic;
        TextView fav_contact_name,fav_contact_no,fav_first_ltter;
        ImageView fav_fav_img;//fav_unfav_img;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            fav_profile_pic = itemView.findViewById(R.id.fav_profile_pic);
            fav_first_ltter = itemView.findViewById(R.id.fav_first_ltter);
            fav_contact_name = itemView.findViewById(R.id.fav_contact_name);
            fav_contact_no =itemView.findViewById(R.id.fav_contact_no);

            fav_fav_img= itemView.findViewById(R.id.fav_fav_img);
          //  fav_unfav_img = itemView.findViewById(R.id.fav_unfav_img);
        }
    }
}
