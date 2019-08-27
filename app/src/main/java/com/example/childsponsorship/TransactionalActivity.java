package com.example.childsponsorship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TransactionalActivity extends AppCompatActivity implements TokenChange {
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    FragmentManager manager;
    FragmentTransaction transaction;
    User user;
    User currentUser;
    String usertoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
       mAuth  = FirebaseAuth.getInstance();
       firebaseUser = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("User");
        currentUser = user;
if (getIntent()!= null){
    usertoken = getIntent().getStringExtra("token");
}




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    user = snapshot.getValue(User.class);
                    Log.e("val", user.getUserId() + firebaseUser.getUid());




                    if ((user.getUserId().contentEquals(firebaseUser.getUid())) && user.isSponser()) {
                        Log.e("type", user.isSponser() + "");
                        updateDatabase(user , usertoken);

                        currentUser = user;

                        Fragment fragment = new SponsorFrag();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user" , user);
                        fragment.setArguments(bundle);

                        updateFragment(fragment);
                    }
                    if ((user.getUserId().contentEquals(firebaseUser.getUid())) && !(user.isSponser())) {
                        currentUser = user;
                        updateDatabase(user , usertoken);

                        Fragment fragment = new CollectorFrag();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user" , user);
                        fragment.setArguments(bundle);
                        updateFragment(fragment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TransactionalActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateDatabase(final User user , String token) {
        user.setToken(token);
        final String type ;
        if (user.isSponser()){
            type = "Sponsor";
        }
        else type = "Collector";
        Log.e("user" , user.toString());

        final HashMap<String , Object> result = new HashMap<>();
        result.put("token" , token);
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               Log.e("token" , "updating");
               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
               databaseReference.child(user.getUserId()).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Log.e("token", "onComplete: " + "succesfuly updated" );
                        //   Log.e("assigned_token" , user.getToken());
                       }
                       else
                       {
                           Log.e("token", "onComplete: " + "failed" + task.getException().getMessage() );

                       }
                   }
               });

               DatabaseReference   databaseReference1 = FirebaseDatabase.getInstance().getReference("Department");
               databaseReference1.child(type).child(user.getDepartment()).child(user.getUserId())
                       .updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Log.e("token", "onComplete: " + "succesfuly updated" );
                       }
                       else
                       {
                           Log.e("token", "onComplete: " + "failed" + task.getException().getMessage() );

                       }
                   }
               });
           }
       });

       thread.run();


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
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, new LoginFrag());
                transaction.commit();

        }
        return super.onOptionsItemSelected(item);
    }
    public void updateFragment(Fragment fragment){
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onTokenChanged(String token) {
        Log.e("token_changed" , token);
        updateDatabase(currentUser , token);
    }
}
