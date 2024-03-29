package com.example.childsponsorship.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childsponsorship.Adapter.CollectorAdapter;
import com.example.childsponsorship.R;
import com.example.childsponsorship.bean.Transaction;
import com.example.childsponsorship.bean.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CollectorFrag extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Transaction transaction;
    FirebaseUser user;
    ArrayList<Transaction> data;
    CollectorAdapter adapter;
    User current_user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_collector , container ,false);
        recyclerView = v.findViewById(R.id.recyclerViewCollector);
        user = FirebaseAuth.getInstance().getCurrentUser();
        data = new ArrayList<>();
        if (getArguments()!=null){

            current_user = (User) getArguments().getSerializable("user");
        }
        adapter = new CollectorAdapter(getContext() , data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Transaction")
        .child(current_user.getDepartment()).child("Pending");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (data.size() > 0 ){
                    data.clear();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    transaction = snapshot.getValue(Transaction.class);
                    if (transaction.getCollector_id().contentEquals(user.getUid()) && (transaction.getStatus().equals("Pending")))
                    {
                       /* Log.e("id" , transaction.getCollector_id() +" " + transaction.getCollector_name()
                        + "  " + user.getDisplayName() + " " + user.getUid());*/
                        data.add(transaction);

                    }


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }
}
