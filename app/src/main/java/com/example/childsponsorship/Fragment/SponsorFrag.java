package com.example.childsponsorship.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.childsponsorship.Adapter.SpinnerAdapter;
import com.example.childsponsorship.Adapter.TransactionAdapter;
import com.example.childsponsorship.R;
import com.example.childsponsorship.bean.Transaction;
import com.example.childsponsorship.bean.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SponsorFrag extends Fragment implements View.OnClickListener {
    private static final String SERVER_KEY = "AAAAA6U9zmc:APA91bECOiT_VxbJESSTGNfQeJrJz7IHbtpqucfNakTHc9avFjhSWGesJfPceETyqvsTC6YcwoszJJrFo9lFy2IGw4BanzA8iqXNU6Pif6zRQ3ko_tExGedcaNwB9WeC7XQilN7gfNio";
    FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    TransactionAdapter transactionAdapter;
    User user;
    User collector;
    ArrayList<User> collector_data;
    ArrayList<Transaction> firebase_upload_data;
    ArrayList<Transaction> read_data;
    String FCM_API = "https://fcm.googleapis.com/fcm/send";
    RequestQueue requestQueue;
    Transaction transaction_submitted;
    Transaction readTransaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_sponsor , container , false);
        floatingActionButton = v.findViewById(R.id.fab);
        recyclerView = v.findViewById(R.id.recyclerViewSponsor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        read_data = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(getContext() , read_data);
        recyclerView.setAdapter(transactionAdapter);
        requestQueue = Volley.newRequestQueue(getContext());
        if (getArguments()!=null){

            user = (User) getArguments().getSerializable("user");
        }

        loadtorecyclerview();

        firebase_upload_data = new ArrayList<>();
        readFromDatabase();

        floatingActionButton.setOnClickListener(this);
        return v;
    }

    private void loadCurrentUser() {
        DatabaseReference myRef;
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User temp_user = snapshot.getValue(User.class);
                    if (temp_user.getUserId().contentEquals(firebaseUser.getUid())){
                        user = temp_user;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                onPopupClicked(v);
                break;


        }
    }

    private void readFromDatabase() {
        collector_data = new ArrayList<>();
        databaseReference  = FirebaseDatabase.getInstance().getReference("Department")
                .child("Collector").child(user.getDepartment());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    collector = snapshot.getValue(User.class);

                    collector_data.add(collector);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onPopupClicked(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.mycustompopup, null);
        final Spinner spinner  = popupView.findViewById(R.id.collector_name);
        final TextInputEditText editText = popupView.findViewById(R.id.amount);
        Button button = popupView.findViewById(R.id.submit);
        spinner.setPrompt("Select Collector Name");


        final SpinnerAdapter adapter = new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, collector_data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                collector = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = editText.getText().toString();

                setValue(amount);

                //value published firebase
                Log.e("amount" , amount);
                databaseReference = FirebaseDatabase.getInstance().getReference("Transaction")
                .child(user.getDepartment()).child("Pending");
                databaseReference.child(transaction_submitted.getKey()).setValue(transaction_submitted);

                Log.e("key" , "Loading to Firebase");

                read_data.clear();
                transactionAdapter.notifyDataSetChanged();
               prepareNotification(collector.getToken());

                popupWindow.dismiss();
            }
        });
    }

    private void prepareNotification(String token) {
        JSONObject notfication = new JSONObject();
        JSONObject notficationBody = new JSONObject();
        try {
            notficationBody.put("title", "You have recieved payment");
            notficationBody.put("body",user.getName() + " has handed over "+ transaction_submitted.getAmount());
            notfication.put("to" , token);
            notfication.put("data", notficationBody);
            notfication.put("notification" , notficationBody);

        }
        catch (Exception e){
            Log.e("Exception Notification", e.getMessage());
        }
        sendNotification(notfication);
    }

    private void sendNotification(JSONObject notfication) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST , FCM_API, notfication,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    Log.e("Response Success" , response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response Error" , error.toString());

                    }

                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String contenttype = "application/json";
               HashMap<String , String> param = new HashMap();
               param.put("Authorization", "key=" + SERVER_KEY);
               param.put("Content-Type" , contenttype);
               return param;
            }
        };

requestQueue.add(request);
    }


    private void loadtorecyclerview() {

        Log.e("key" , "In loading");
        databaseReference  = FirebaseDatabase.getInstance().getReference("Transaction")
                .child(user.getDepartment())
        .child("Pending");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    readTransaction = snapshot.getValue(Transaction.class);
                    if (readTransaction.getSponsor_id().contentEquals(user.getUserId())) {
                      read_data.add(readTransaction);
                      Log.e("log" , readTransaction.toString());
                    }
                }

                   transactionAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setValue(String amount) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = simpleDateFormat.format(new Date());

        transaction_submitted = new Transaction();
        transaction_submitted.setCollector_id(collector.getUserId());
        transaction_submitted.setCollector_name(collector.getName());
        transaction_submitted.setDepartment(collector.getDepartment());
        transaction_submitted.setSponsor_id(user.getUserId());
        transaction_submitted.setSponsor_name(user.getName());
        transaction_submitted.setAmount(amount);
        transaction_submitted.setPublished_at(format);
        transaction_submitted.setStatus("Pending");
        transaction_submitted.setKey(databaseReference.push().getKey());
        transaction_submitted.setSponsor_token(user.getToken());
        transaction_submitted.setCollector_token(collector.getToken());
        firebase_upload_data.add(transaction_submitted);
    }
}
