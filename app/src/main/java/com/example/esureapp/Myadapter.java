package com.example.esureapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {

    Context context;
    ArrayList<Profile> list;

    public Myadapter(Context context, ArrayList<Profile> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Profile user = list.get(position);
        holder.rfid.setText(user.getRfid());
        holder.username.setText(user.getUsername());
        holder.name.setText(user.getName());
        holder.amount.setText(String.valueOf(user.getAmount())); // Convert amount to String
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView amount, email, name, username, rfid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            email = itemView.findViewById(R.id.email);
            name = itemView.findViewById(R.id.Name);
            username = itemView.findViewById(R.id.username);
            rfid = itemView.findViewById(R.id.RFID);
        }
    }
}
