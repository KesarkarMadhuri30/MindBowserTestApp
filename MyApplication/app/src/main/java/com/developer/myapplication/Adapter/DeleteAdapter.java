package com.developer.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
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
import androidx.recyclerview.widget.RecyclerView;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.viewHolder> {
    Activity context;
    List<ContactModel> mDeleteContactList;
    String strFirstletter;
    AlertDialog dialog;

    public DeleteAdapter(Activity context, List<ContactModel> mDeleteContactList) {
        this.context = context;
        this.mDeleteContactList = mDeleteContactList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.delete_cont_listitem,parent,false);
        return new viewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        String imagepath = mDeleteContactList.get(position).getImagepath();
        if (imagepath != null) {
            holder.del_profile_pic.setVisibility(View.VISIBLE);
            holder.del_first_ltter.setVisibility(View.GONE);
            Glide.with(context).load(imagepath).into(holder.del_profile_pic);
        }else {
            holder.del_profile_pic.setVisibility(View.GONE);
            holder.del_first_ltter.setVisibility(View.VISIBLE);
            strFirstletter = mDeleteContactList.get(position).getContactname();
            strFirstletter = strFirstletter.substring(0,1);
            holder.del_first_ltter.setText(""+strFirstletter);
        }
        holder.del_contact_name.setText(mDeleteContactList.get(position).getContactname());
        holder.del_contact_no.setText(mDeleteContactList.get(position).getContactphone());

        holder.del_restore_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alertmsg = "Do you really want to Restore?";
                RestoreAlertdialog(alertmsg,position);

            }
        });
    }

    private void RestoreAlertdialog(String alertmsg, int position) {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);

        LayoutInflater inflater = context.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.delete_alert_dialog, null);
        final TextView txt_dialog = alertLayout.findViewById(R.id.txt_dialog);
        final TextView btn_yes = alertLayout.findViewById(R.id.btn_yes);
        final TextView btn_no = alertLayout.findViewById(R.id.btn_no);
        btn_yes.setText("Restore");
        btn_no.setText("Cancel");


        txt_dialog.setText(""+alertmsg);

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
                setRestoreContact(position);
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

    private void setRestoreContact(int position) {
        class RestoreContactTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .contactListDao()
                        .update_deleteStatus(0,mDeleteContactList.get(position).getAuto_id());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifyDataSetChanged();
                mDeleteContactList.remove(position);
                Toast.makeText(context, "Contact Restored ", Toast.LENGTH_LONG).show();
            }
        }

        RestoreContactTask dt = new RestoreContactTask();
        dt.execute();
    }

    @Override
    public int getItemCount() {
        return mDeleteContactList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView del_profile_pic;
        TextView del_contact_name,del_contact_no,del_first_ltter;
        ImageView del_restore_img;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            del_profile_pic = itemView.findViewById(R.id.del_profile_pic);
            del_first_ltter = itemView.findViewById(R.id.del_first_ltter);
            del_contact_name = itemView.findViewById(R.id.del_contact_name);
            del_contact_no =itemView.findViewById(R.id.del_contact_no);

            del_restore_img = itemView.findViewById(R.id.del_restore_img);
        }
    }
}
