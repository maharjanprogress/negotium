package com.example.negotium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.negotium.databinding.ActivityClientRequestBinding;
import com.example.negotium.databinding.ActivitySortByUserBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SortByUser extends AppCompatActivity implements RecyclerViewInterface{
    ActivitySortByUserBinding binding;
    private ProgressDialog progressDialog;
    RecyclerView rvItem;

    ItemUserAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySortByUserBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setTitle("Requests View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        rvItem = findViewById(R.id.rv_item);
        binding.sortdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ClientRequest.class);
                startActivity(intent);
            }
        });
        buildItemList();
    }

    private void buildItemList() {
        progressDialog.setMessage("not showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_GETBUYUSER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("username");
                                JSONArray jsonArray0 = (JSONArray)obj.getJSONArray("phoneno");
                                JSONArray jsonArray00 = (JSONArray)obj.getJSONArray("id");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("buyid");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("buydate1");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("quantity");
                                JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("username1");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("phoneno1");
                                JSONArray jsonArray6 = (JSONArray)obj.getJSONArray("product_name");
                                JSONArray jsonArray7 = (JSONArray)obj.getJSONArray("pic");
                                JSONArray jsonArray8 = (JSONArray)obj.getJSONArray("totalprice");
                                if(jsonArray != null){
                                    List<ItemUser> itemList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        List<SubItemUser> subItemList = new ArrayList<>();
                                        for (int j=0; j<jsonArray4.length(); j++) {
//                                            SubItem subItem = new SubItem("one");
//                                            subItemList.add(subItem);
                                            if (Objects.equals(jsonArray4.getString(j), jsonArray.getString(i))){
                                                SubItemUser subItem = new SubItemUser(jsonArray2.getString(j), jsonArray1.getString(j),jsonArray3.getString(j),jsonArray5.getString(j),jsonArray6.getString(j),jsonArray7.getString(j),jsonArray8.getString(j));
                                                subItemList.add(subItem);
                                            }

                                        }

                                        ItemUser item = new ItemUser(jsonArray.getString(i),jsonArray0.getString(i),jsonArray00.getString(i) ,subItemList);
                                        itemList.add(item);
                                    }
                                    send(itemList);


                                }
                                else{
                                    Toast.makeText(SortByUser.this, "There is no more product delivery request", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }else{
                                Toast.makeText(SortByUser.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SortByUser.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void send(List<ItemUser> itemList) {
        itemAdapter = new ItemUserAdapter(itemList,this,this);
        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {
        sendcommand(position,"1");
    }

    private void sendcommand(int buyid, String isbought) {
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_ADMINSUBITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(s);
                            Toast.makeText(SortByUser.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),SortByUser.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SortByUser.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("isbought",isbought);
                params.put("buyid", String.valueOf(buyid));
                return params;
            }
        };
//        for (int i = 0; i < 3; i++) {
//            Item item = new Item("jsonArray.getString(i)", buildSubItemList());
//            itemList.add(item);
//        }
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onItemUpdate(int position) {

    }

    @Override
    public void onItemDelete(int position) {
        sendcommand(position,"0");
    }

    @Override
    public void onDateAccept(String date) {
        senddate(date,"1");
    }

    private void senddate(String date, String number) {
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_ADMINITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(s);
                            Toast.makeText(SortByUser.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),SortByUser.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SortByUser.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("isbought",number);
                params.put("id", date);
                return params;
            }
        };
//        for (int i = 0; i < 3; i++) {
//            Item item = new Item("jsonArray.getString(i)", buildSubItemList());
//            itemList.add(item);
//        }
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onDateDecline(String date) {
        senddate(date,"0");
    }
}