package com.example.childsponsorship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.childsponsorship.Fragment.CollectorFrag;
import com.example.childsponsorship.Fragment.LoginFrag;
import com.example.childsponsorship.Fragment.SponsorFrag;
import com.example.childsponsorship.bean.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TransactionalActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    FragmentManager manager;
    FragmentTransaction transaction;
    User user;
    String usertoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
       mAuth  = FirebaseAuth.getInstance();
       firebaseUser = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("User");

if (getIntent()!= null){
    usertoken = getIntent().getStringExtra("token");
}




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    user = snapshot.getValue(User.class);
                    Log.e("val", user.getUserId() + firebaseUser.getUid());


                    if(user.getUserId().contentEquals(firebaseUser.getUid())){
                        updateDatabase(user , usertoken);
                    }

                    if (user.getUserId().contentEquals(firebaseUser.getUid()) && user.isSponser()) {
                        Log.e("type", user.isSponser() + "");
                        Fragment fragment = new SponsorFrag();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user" , user);
                        fragment.setArguments(bundle);

                        updateFragment(fragment);
                    }
                    if (user.getUserId().contentEquals(firebaseUser.getUid()) && !(user.isSponser())) {
                        updateFragment(new CollectorFrag());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TransactionalActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateDatabase(User user , String token) {
        user.setToken(token);
        String type ;
        if (user.isSponser()){
            type = "Sponsor";
        }
        else type = "Collector";
        Log.e("user" , user.toString());

        HashMap<String , Object> result = new HashMap<>();
        result.put("token" , token);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(user.getUserId()).updateChildren(result);

            databaseReference = FirebaseDatabase.getInstance().getReference("Department")
                    .child(type).child(user.getDepartment())

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                updateFragment(new LoginFrag());

        }
        return super.onOptionsItemSelected(item);
    }
    public void updateFragment(Fragment fragment){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }
}
