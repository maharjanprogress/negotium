package com.example.negotium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.negotium.databinding.ActivityChatBinding;
import com.example.negotium.databinding.ActivityChatListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Chat extends AppCompatActivity implements RecyclerViewInterface{
    ActivityChatBinding binding;
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ConstraintLayout rootLayout = findViewById(R.id.main);
        String id= getIntent().getStringExtra("id");
        String usernam= getIntent().getStringExtra("usernam");
        progressDialog = new ProgressDialog(this);
        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setTitle(usernam);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclechat);
        showlist(id);
        binding.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.message.getText().toString();
                sendbtn(text,id);
            }
        });
//        recyclerView.post(() -> recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1));
//        List<MessageList> messageLists = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            MessageList the = new MessageList();
//            the.date="2002-03-22";
//            the.message="I love you";
//            the.sentbyuser=true;
//            messageLists.add(the);
//        }
//        for (int i = 0; i < 3; i++) {
//            MessageList the = new MessageList();
//            the.date="2002-03-23";
//            the.message="I love you too";
//            the.sentbyuser=false;
//            messageLists.add(the);
//        }

    }

    private void sendbtn(String text, String id) {
        progressDialog.setMessage("Uploading chat...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MESSAGEFROMADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    Toast.makeText(Chat.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    binding.message.setText("");
                    showlist(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(Chat.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put("text",text);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void showlist(String id) {
        progressDialog.setMessage("Loading Chats...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_MESSAGELIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("id");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("text");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("date");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("isseenuser");
                                JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("isseen");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("sentbyuser");
                                if(jsonArray != null){
                                    List<MessageList> messageLists = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        MessageList the = new MessageList();
                                        the.message=jsonArray1.getString(i);
                                        the.date=jsonArray2.getString(i);
                                        if (Objects.equals(jsonArray5.getString(i), "0")){
                                            the.sentbyuser=false;
                                        }else {
                                            the.sentbyuser=true;
                                        }
                                        if (Objects.equals(jsonArray3.getString(i), "0")){
                                            the.isseenuser=false;
                                        }else {
                                            the.isseenuser=true;
                                        }
                                        if (Objects.equals(jsonArray4.getString(i), "0")){
                                            the.isseenadmin=false;
                                        }else {
                                            the.isseenadmin=true;
                                        }
                                        messageLists.add(the);
                                    }
                                    send(messageLists);


                                }
                                else{
                                    Toast.makeText(Chat.this, "There are no Chat", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }else{
                                Toast.makeText(Chat.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Chat.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };
//        for (int i = 0; i < 3; i++) {
//            Item item = new Item("jsonArray.getString(i)", buildSubItemList());
//            itemList.add(item);
//        }
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void send(List<MessageList> messageLists) {
        messageAdapter = new MessageAdapter(messageLists,this,true);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemUpdate(int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    public void onDateAccept(String date) {

    }

    @Override
    public void onDateDecline(String date) {

    }
}