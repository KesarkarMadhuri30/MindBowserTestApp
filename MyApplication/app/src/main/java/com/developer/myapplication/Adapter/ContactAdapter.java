package com.developer.myapplication.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Vibrator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.myapplication.Database.DatabaseClient;
import com.developer.myapplication.Model.ContactModel;
import com.developer.myapplication.R;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Viewholder> implements Filterable {
    Activity context;
    List<ContactModel> mContactList;
    List<ContactModel> mSearchContactList;
    String strFirstletter;

    AlertDialog dialog;
    private boolean isAllFragment;

    public ContactAdapter(Activity context, List<ContactModel> mContactList, boolean isAllFragment) {
        this.context = context;
        this.mContactList = mContactList;
        this.mSearchContactList =mContactList;
        this.isAllFragment = isAllFragment;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.contact_listitem, parent, false);
        return new Viewholder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String imagepath = mContactList.get(position).getImagepath();
        if (imagepath != null) {
            holder.profile_pic.setVisibility(View.VISIBLE);
            holder.first_ltter.setVisibility(View.GONE);
            Glide.with(context).load(imagepath).into(holder.profile_pic);
        } else {
            holder.profile_pic.setVisibility(View.GONE);
            holder.first_ltter.setVisibility(View.VISIBLE);
            strFirstletter = mContactList.get(position).getContactname();
            strFirstletter = strFirstletter.substring(0, 1);
            holder.first_ltter.setText("" + strFirstletter);
        }
        holder.contact_name.setText(mContactList.get(position).getContactname());
        holder.contact_no.setText(mContactList.get(position).getContactphone());

        if (mContactList.get(position).getFav_status() == 1) {
            holder.fav_img.setVisibility(View.VISIBLE);
            holder.unfav_img.setVisibility(View.GONE);
        } else {
            holder.fav_img.setVisibility(View.GONE);
            holder.unfav_img.setVisibility(View.VISIBLE);
        }




        holder.unfav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                class SaveTask extends AsyncTask<Void, Void, Void> {


                    @Override
                    protected Void doInBackground(Void... voids) {

                        //adding to database
                        DatabaseClient.getInstance(context).getAppDatabase()
                                .contactListDao()
                                .updatefavStatus(1, mContactList.get(position).getAuto_id());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {

                        holder.unfav_img.setVisibility(View.GONE);
                        holder.fav_img.setVisibility(View.VISIBLE);

                       // mDataListener.contactListUpdate();
                        //Toast.makeText(context, "Saved "+mContactList.get(position).getFav_status(), Toast.LENGTH_LONG).show();
                    }
                }
                SaveTask st = new SaveTask();
                st.execute();
            }
        });

        holder.fav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class UnsavedTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        DatabaseClient.getInstance(context).getAppDatabase()
                                .contactListDao()
                                .updatefavStatus(0, mContactList.get(position).getAuto_id());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {

                        holder.fav_img.setVisibility(View.GONE);
                        holder.unfav_img.setVisibility(View.VISIBLE);

                        //Toast.makeText(context, "Deleted"+mContactList.get(position).getAuto_id(), Toast.LENGTH_LONG).show();
                        //mDataListener.contactListUpdate();

                    }
                }

                UnsavedTask dt = new UnsavedTask();
                dt.execute();
            }
        });


        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String alertmsg = "Do you really want to Delete?";
                DeleteAlertdialog(alertmsg, position);


            }
        });

        holder.contact_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = holder.contact_no.getText().toString();

                setChoiceDialog(phoneNumber);


             /*   Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                Intent editIntent = new Intent(Intent.ACTION_DIAL);
                viewIntent.setData(Uri.parse("sms:" + phoneNumber));
                editIntent.setData(Uri.parse("tel:" + phoneNumber) );
                Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { editIntent });
                context.startActivity(chooserIntent);*/
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


    private void DeleteAlertdialog(String alertmsg, int position) {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);

        LayoutInflater inflater = context.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.delete_alert_dialog, null);
        final TextView txt_dialog = alertLayout.findViewById(R.id.txt_dialog);
        final TextView btn_yes = alertLayout.findViewById(R.id.btn_yes);
        final TextView btn_no = alertLayout.findViewById(R.id.btn_no);
        btn_yes.setText("Delete");
        btn_no.setText("Cancel");


        txt_dialog.setText("" + alertmsg);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);


        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        dialog.setCancelable(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                setdeleteContact(position);
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

    private void setdeleteContact(int position) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .contactListDao()
                        .update_deleteStatus(1, mContactList.get(position).getAuto_id());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(context, "Contact Deleted", Toast.LENGTH_LONG).show();
                mContactList.remove(position);
                notifyDataSetChanged();

            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mContactList = mSearchContactList;
                } else {

                    List<ContactModel> filteredList = new ArrayList<>();

                    for (ContactModel userModel : mSearchContactList) {

                        if (userModel.getContactname().toLowerCase().contains(charString)
                                ||userModel.getContactphone().toLowerCase().contains(charString)) {
                            filteredList.add(userModel);
                        }
                    }

                    mContactList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mContactList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mContactList = (List<ContactModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView profile_pic;
        TextView contact_name, contact_no, first_ltter;
        ImageView fav_img, unfav_img, delete_img;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            profile_pic = itemView.findViewById(R.id.profile_pic);
            first_ltter = itemView.findViewById(R.id.first_ltter);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_no = itemView.findViewById(R.id.contact_no);

            fav_img = itemView.findViewById(R.id.fav_img);
            unfav_img = itemView.findViewById(R.id.unfav_img);
            delete_img = itemView.findViewById(R.id.delete_img);
        }
    }
}
