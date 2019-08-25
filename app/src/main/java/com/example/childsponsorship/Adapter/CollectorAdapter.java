package com.example.childsponsorship.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childsponsorship.R;
import com.example.childsponsorship.bean.Transaction;
import com.example.childsponsorship.bean.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CollectorAdapter extends RecyclerView.Adapter<CollectorAdapter.ViewHolder> {
    Context context;
    ArrayList<Transaction> data;

    public CollectorAdapter(Context context, ArrayList<Transaction> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item_collector_rv  , parent , false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    holder.amount.setText(data.get(position).getAmount());
  //  holder.published_at.setText(data.get(position));
    holder.sender.setText(data.get(position).getSponsor_name());
    holder.published_at.setText(data.get(position).getPublished_at());

    holder.accepted.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Transaction transaction = data.get(position);
          transaction.setStatus("Accepted");
            HashMap<String , Object> map = new HashMap<>();
            map.put("status" , transaction.getStatus());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Transaction")
                    .child(transaction.getDepartment()).child("Pending").child(transaction.getKey());
            databaseReference.updateChildren(map);

            data.remove(position);
        }
    });

    holder.rejected.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Transaction transaction = data.get(position);
            transaction.setStatus("Rejected");

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Transaction")
                    .child(transaction.getDepartment()).child("Rejected").child(transaction.getKey());
            databaseReference.setValue(transaction);
            data.remove(position);
            databaseReference =FirebaseDatabase.getInstance().getReference("Transaction")
                    .child(transaction.getDepartment()).child("Pending").child(transaction.getKey());
            databaseReference.removeValue();
        }
    });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView published_at;
        TextView amount;
        TextView sender;
        ImageView accepted;
        ImageView rejected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            published_at = itemView.findViewById(R.id.timestamp);
            amount = itemView.findViewById(R.id.amount);
            sender = itemView.findViewById(R.id.RecievedFrom);
            accepted = itemView.findViewById(R.id.amount_accept);
            rejected = itemView.findViewById(R.id.amount_reject);
        }
    }
}
