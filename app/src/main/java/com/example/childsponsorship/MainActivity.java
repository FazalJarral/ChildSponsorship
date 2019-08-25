package com.example.childsponsorship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.childsponsorship.Fragment.LoginFrag;
import com.example.childsponsorship.bean.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    private String TAG = "MAINACTIVITY";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            token = task.getResult().getToken();

                            Intent intent = new Intent(MainActivity.this, TransactionalActivity.class);
                            Log.e("token", token);
                            intent.putExtra("token", token);
                            startActivity(intent);
                            // Log and toast
                            Log.d(TAG, token);
                            Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
            updateFragment(new LoginFrag());
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
    @Override
    protected void onStart() {
        super.onStart();

    }
   public void updateFragment(Fragment fragment){
      manager = getSupportFragmentManager();
         transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

}
