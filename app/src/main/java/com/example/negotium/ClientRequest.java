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
import com.example.negotium.databinding.ActivityClientRequestBinding;
import com.example.negotium.databinding.ActivityProductlookBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClientRequest extends AppCompatActivity implements RecyclerViewInterface{
    ActivityClientRequestBinding binding;
    private ProgressDialog progressDialog;
    RecyclerView rvItem;

    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientRequestBinding.inflate(getLayoutInflater());
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
        buildItemList();
    }
    private void buildItemList() {

        progressDialog.setMessage("not showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_GETBUYDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("buydate");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("buyid");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("buydate1");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("quantity");
                                JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("username");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("phoneno");
                                JSONArray jsonArray6 = (JSONArray)obj.getJSONArray("product_name");
                                JSONArray jsonArray7 = (JSONArray)obj.getJSONArray("pic");
                                JSONArray jsonArray8 = (JSONArray)obj.getJSONArray("totalprice");
                                if(jsonArray != null){
                                    List<Item> itemList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        List<SubItem> subItemList = new ArrayList<>();
                                        for (int j=0; j<jsonArray2.length(); j++) {
//                                            SubItem subItem = new SubItem("one");
//                                            subItemList.add(subItem);
                                            if (Objects.equals(jsonArray2.getString(j), jsonArray.getString(i))){
                                                SubItem subItem = new SubItem(jsonArray4.getString(j), jsonArray1.getString(j),jsonArray3.getString(j),jsonArray5.getString(j),jsonArray6.getString(j),jsonArray7.getString(j),jsonArray8.getString(j));
                                                subItemList.add(subItem);
                                            }

                                        }

                                        Item item = new Item(jsonArray.getString(i), subItemList);
                                        itemList.add(item);
                                    }
                                    send(itemList);


                                }
                                else{
                                    Toast.makeText(ClientRequest.this, "There is no more product delivery request", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }else{
                                Toast.makeText(ClientRequest.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ClientRequest.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void send(List<Item> itemList) {
        itemAdapter = new ItemAdapter(itemList,this,this);
        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
    }
//
//    private List<SubItem> buildSubItemList() {
//        List<SubItem> subItemList = new ArrayList<>();
//        for (int i=0; i<10; i++) {
//            SubItem subItem = new SubItem("Sub Item ", "Description ");
//            subItemList.add(subItem);
//        }
//        return subItemList;
//    }

    @Override
    public void onItemClick(int position) {
        sendcommand(position,"1");
    }

    private void sendcommand(int buyid,String isbought) {
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
                            Toast.makeText(ClientRequest.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),ClientRequest.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ClientRequest.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ClientRequest.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),ClientRequest.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ClientRequest.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("isbought",number);
                params.put("buydate", date);
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