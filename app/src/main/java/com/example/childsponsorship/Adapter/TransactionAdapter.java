package com.example.childsponsorship.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childsponsorship.R;
import com.example.childsponsorship.bean.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    Context context;
    ArrayList<Transaction> data;

    public TransactionAdapter(Context context, ArrayList<Transaction> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(context).inflate(R.layout.single_item_sponsor_rv, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = data.get(position);
        holder.amount.setText(transaction.getAmount());
        holder.collector.setText(transaction.getCollector_name());
        holder.published.setText(transaction.getPublished_at());
        holder.status.setText(transaction.getStatus());
        switch (transaction.getStatus()) {
            case "Pending":
                holder.status.setTextColor(Color.BLUE);
                break;
            case "Accepted":
                holder.status.setTextColor(Color.GREEN);

                break;
            case "Rejected":
                holder.status.setTextColor(Color.RED);

                break;
        }
    }

    @Override
    public int getItemCount() {

        return data.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView published;
        TextView amount;
        TextView collector;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            published = itemView.findViewById(R.id.timestamp);
            amount = itemView.findViewById(R.id.amount);
            collector = itemView.findViewById(R.id.collector);
            status = itemView.findViewById(R.id.status);
        }
    }
}
