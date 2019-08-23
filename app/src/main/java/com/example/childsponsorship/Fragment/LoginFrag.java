package com.example.childsponsorship.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.childsponsorship.MainActivity;
import com.example.childsponsorship.R;
import com.example.childsponsorship.TransactionalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFrag extends Fragment {
    TextView register;
    FirebaseAuth mAuth;
    TextInputEditText email;
    TextInputEditText password;
    Button login;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_login , container , false);
        register = v.findViewById(R.id.tv_register);
        mAuth = FirebaseAuth.getInstance();
        email = v.findViewById(R.id.email);

        password = v.findViewById(R.id.password);
        login = v.findViewById(R.id.buttonlogin);
        progressBar = v.findViewById(R.id.progress_bar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email.getText().toString() , password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    updateUI(mAuth.getCurrentUser());
                                }
                                else {
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }}
                        });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).updateFragment(new SignupFrag());
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
    //   updateUI(currentUser);
    }

    //Move To Next Activity
    private void updateUI(FirebaseUser user) {
       Intent intent = new Intent(getContext() , TransactionalActivity.class);
        intent.putExtra("user" , user);
//        Log.e("user" , user.getUid());
        startActivity(intent);
    }

}
