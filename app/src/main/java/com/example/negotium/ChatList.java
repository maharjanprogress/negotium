package com.example.negotium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.negotium.databinding.ActivityAdminPageBinding;
import com.example.negotium.databinding.ActivityChatListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatList extends AppCompatActivity implements RecyclerViewInterface{
    ActivityChatListBinding binding;
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    ChatListAdapter chatListAdapter;
    List<ChatClass> chatClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);
        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.chatrecycle);
        showlist();
    }

    private void showlist() {
        progressDialog.setMessage("Loading Chats...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_ADMINTEXTLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("id");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("text");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("username");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("phoneno");
                                JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("isseen");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("sentbyuser");
                                if(jsonArray != null){
                                    chatClasses = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ChatClass the = new ChatClass();
                                        the.id=jsonArray.getString(i);
                                        the.username=jsonArray2.getString(i);
                                        the.text=jsonArray1.getString(i);
                                        the.phoneno=jsonArray3.getString(i);
                                        if (Objects.equals(jsonArray4.getString(i), "0")){
                                            the.isseen=false;
                                        }else {
                                            the.isseen=true;
                                        }
                                        if (Objects.equals(jsonArray5.getString(i), "0")){
                                            the.sentbyuser=false;
                                        }else {
                                            the.sentbyuser=true;
                                        }
                                        chatClasses.add(the);
                                    }
                                    send(chatClasses);


                                }
                                else{
                                    Toast.makeText(ChatList.this, "There are no Chat Heads", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }else{
                                Toast.makeText(ChatList.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ChatList.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
//                params.put("password",password);
                return params;
            }
        };
//        for (int i = 0; i < 3; i++) {
//            Item item = new Item("jsonArray.getString(i)", buildSubItemList());
//            itemList.add(item);
//        }
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void send(List<ChatClass> chatClasses) {
        chatListAdapter = new ChatListAdapter(chatClasses,this,this);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), Chat.class);
        intent.putExtra("id",chatClasses.get(position).id);
        intent.putExtra("usernam",chatClasses.get(position).username);
        startActivity(intent);
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