package com.example.childsponsorship.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.example.childsponsorship.MainActivity;
import com.example.childsponsorship.R;
import com.example.childsponsorship.bean.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignupFrag extends Fragment implements View.OnClickListener {
    TextView already_account;
    TextInputEditText name;
    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText confirm_password;
    AppCompatSpinner spinner;
    RadioButton sponsor, collector;
    Button signup;
    FirebaseAuth mAuth;
    ArrayList<String> dept;
    String selected_dept;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_signup, container, false);
        already_account = v.findViewById(R.id.already_account);
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        password = v.findViewById(R.id.password);
        confirm_password = v.findViewById(R.id.confirm_password);
        spinner = v.findViewById(R.id.department);
        spinner.setPrompt("Select Your Department");
        sponsor = v.findViewById(R.id.rb_sponsor);
        collector = v.findViewById(R.id.rb_collector);
        signup = v.findViewById(R.id.btnsignup);
        mAuth = FirebaseAuth.getInstance();
        dept = new ArrayList<>();

        initializedept();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dept);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_dept = dept.get(position);
                Log.e("selected", selected_dept);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        already_account.setOnClickListener(this);
        signup.setOnClickListener(this);
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

    private void initializedept() {
        dept.add("Software");
        dept.add("Civil");
        dept.add("Mechanical");
        dept.add("Electrical");
        dept.add("Computer Engineering");
        dept.add("Telecom");
        dept.add("Computer Science");
        dept.add("Industrial");
        dept.add("Electronics");
        dept.add("Environmental Engineering");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.already_account:
                ((MainActivity) getActivity()).updateFragment(new LoginFrag());
                break;
            case R.id.btnsignup:
                boolean isValidate = validate();
                if (isValidate) {
                    Toast.makeText(getContext(), "Create Account", Toast.LENGTH_SHORT).show();
                    createAccount(email.getText().toString(), password.getText().toString());
                }
        }
    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    addtoDatabase(user);
                    // updateUI(user);
                } else
                    Log.e("Error", task.getException().getLocalizedMessage());
            }
        });

    }

    private void addtoDatabase(final FirebaseUser firebaseUser) {
        User user = new User();
        if (selected_dept == null) {
            selected_dept = "Software";
        }
        user.setDepartment(selected_dept);
        user.setEmail(firebaseUser.getEmail());
        user.setName(name.getText().toString());

        if (sponsor.isChecked()) {
            user.setSponser(true);
        } else {
            user.setSponser(false);
        }
        user.setUserId(firebaseUser.getUid());
        Log.e("selected", selected_dept);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
        if (user.isSponser()) {
            DatabaseReference dept_ref = database.getReference("Department").child("Sponsor");


        //Create A department in db
        dept_ref.child(user.getDepartment()).child(user.getUserId()).
                setValue(user);
    }
         else
        {
            DatabaseReference dept_ref = database.getReference("Department").child("Collector");


            //Create A department in db
            dept_ref.child(user.getDepartment()).child(user.getUserId()).
                    setValue(user);
        }


        //send user in db
        myRef.child(user.getUserId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateUI(firebaseUser);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("fazal", e.getLocalizedMessage());
                    }
                });

    }

    private void updateUI(FirebaseUser user) {

        Toast.makeText(getContext(), "Updating UI", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).updateFragment(new LoginFrag());


    }

    private boolean validate() {
        String email_text = email.getText().toString();
        String name_text = name.getText().toString();
        String pswrd = password.getText().toString();
        String confirm_pswrd = confirm_password.getText().toString();

        if (TextUtils.isEmpty(email_text)) {
            email.setError("Cannot Be Empty");
            return false;
        }
        if (TextUtils.isEmpty(name_text)) {
            name.setError("Cannot Be Empty");
            return false;
        }
        if (TextUtils.isEmpty(pswrd)) {
            password.setError("Cannot Be Empty");
            return false;
        }
        if (TextUtils.isEmpty(confirm_pswrd)) {
            confirm_password.setError("Cannot Be Empty");
            return false;

        }

        if (!pswrd.contentEquals(confirm_pswrd)) {
            confirm_password.setError("Password Dont Match");
            return false;
        }


        Log.e("spinner", spinner.getSelectedItemId() + "");

        return true;


    }
}
