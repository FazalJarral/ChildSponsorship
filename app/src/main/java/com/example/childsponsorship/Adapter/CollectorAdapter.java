package com.example.childsponsorship.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.childsponsorship.R;
import com.example.childsponsorship.bean.Transaction;
import com.example.childsponsorship.bean.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectorAdapter extends RecyclerView.Adapter<CollectorAdapter.ViewHolder> {
    Context context;
    ArrayList<Transaction> data;
    private static final String SERVER_KEY = "AAAAA6U9zmc:APA91bECOiT_VxbJESSTGNfQeJrJz7IHbtpqucfNakTHc9avFjhSWGesJfPceETyqvsTC6YcwoszJJrFo9lFy2IGw4BanzA8iqXNU6Pif6zRQ3ko_tExGedcaNwB9WeC7XQilN7gfNio";
    String FCM_API = "https://fcm.googleapis.com/fcm/send";
    RequestQueue requestQueue;
    public CollectorAdapter(Context context, ArrayList<Transaction> data) {
        this.context = context;
        this.data = data;
        requestQueue = Volley.newRequestQueue(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item_collector_rv  , parent , false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    holder.amount.setText(data.get(position).getAmount());
  //  holder.published_at.setText(data.get(position));
    holder.sender.setText(data.get(position).getSponsor_name());
    holder.published_at.setText(data.get(position).getPublished_at());

    holder.accepted.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Transaction transaction = data.get(position);
          transaction.setStatus("Accepted");
            HashMap<String , Object> map = new HashMap<>();
            map.put("status" , transaction.getStatus());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Transaction")
                    .child(transaction.getDepartment()).child("Pending").child(transaction.getKey());
            databaseReference.updateChildren(map);
            String message  = "Your Transaction Is Accepted";
            data.remove(position);
            prepareNotification(transaction.getSponsor_token() , message );

        }
    });

    holder.rejected.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Transaction transaction = data.get(position);
            transaction.setStatus("Rejected");

            HashMap<String , Object> result = new HashMap<>();
            result.put("status" , transaction.getStatus());
            data.remove(position);
         DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("Transaction")
                    .child(transaction.getDepartment()).child("Pending").child(transaction.getKey());
            databaseReference.updateChildren(result);
            String message = "Your Transaction Was Not Recieved.";
            prepareNotification(transaction.getSponsor_token() , message );
        }
    });

    }
    private void prepareNotification(String token , String message) {
        JSONObject notfication = new JSONObject();
        JSONObject notficationBody = new JSONObject();
        try {
            notficationBody.put("title", message);
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView published_at;
        TextView amount;
        TextView sender;
        ImageView accepted;
        ImageView rejected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            published_at = itemView.findViewById(R.id.timestamp);
            amount = itemView.findViewById(R.id.amount);
            sender = itemView.findViewById(R.id.RecievedFrom);
            accepted = itemView.findViewById(R.id.amount_accept);
            rejected = itemView.findViewById(R.id.amount_reject);
        }
    }
}
