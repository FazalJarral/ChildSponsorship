package com.example.childsponsorship.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childsponsorship.R;
import com.example.childsponsorship.bean.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>{
    Context context;
    ArrayList<Transaction> data;

    public TransactionAdapter(Context context, ArrayList<Transaction> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(context).inflate(R.layout.single_item_sponsor_rv  , parent ,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.amount.setText(data.get(position).getAmount());
        holder.department.setText(data.get(position).getDepartment());
        holder.collector.setText(data.get(position).getCollector_name());
        holder.published.setText(data.get(position).getPublished_at());
    }

    @Override
    public int getItemCount() {

        return data.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView published;
        TextView department;
        TextView amount;
        TextView collector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            published = itemView.findViewById(R.id.timestamp);
            department = itemView.findViewById(R.id.department);
            amount = itemView.findViewById(R.id.amount);
            collector = itemView.findViewById(R.id.collector);
        }
    }
}
