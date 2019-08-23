package com.example.childsponsorship.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.childsponsorship.bean.User;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<User> {

    Context context;
    ArrayList<User> users;
    String name;
    public SpinnerAdapter(Context context, int textViewResourceId,
                       ArrayList<User> users ) {
        super(context, textViewResourceId, users);
        this.context = context;
        this.users = users;
    }
    @Override
    public int getCount(){
        return users.size();
    }

    @Override
    public User getItem(int position){
        return users.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

}
