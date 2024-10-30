package com.example.negotium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class prodictlists extends AppCompatActivity implements RecyclerViewInterface{

    RecyclerView recyclerView;
    DatabaseHelper myDB;
    ArrayList<byte[]> prodpic;
//    ArrayList<Integer> productid,catid;
//    ArrayList<String> productname, price, category, producer,description;

    ArrayList<String> productid, product_name, category, subcate,price,pic,description,producer;
    CustomAadapter customAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prodictlists);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);

        productid = new ArrayList<>();
        product_name = new ArrayList<>();
        category = new ArrayList<>();
        subcate = new ArrayList<>();
        price = new ArrayList<>();
        pic = new ArrayList<>();
        description = new ArrayList<>();
        producer = new ArrayList<>();
        lol();
//
//        myDB = new DatabaseHelper(prodictlists.this);
//        prodpic = new ArrayList<>();
//        productid = new ArrayList<>();
//        productname = new ArrayList<>();
//        price = new ArrayList<>();
//        category = new ArrayList<>();
//        producer = new ArrayList<>();
//        description = new ArrayList<>();
//        catid = new ArrayList<>();

//        storeDataInArrays();
        recyclerView=findViewById(R.id.recyclerView);


    }

    private void lol() {
        progressDialog.setMessage("showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            JSONArray jsonArray = (JSONArray)obj.getJSONArray("productid");
                            JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("product_name");
                            JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("category");
                            JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("subcate");
                            JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("price");
                            JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("pic");
                            JSONArray jsonArray6 = (JSONArray)obj.getJSONArray("description");
                            JSONArray jsonArray7 = (JSONArray)obj.getJSONArray("producer");
                            if(jsonArray != null){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    productid.add(jsonArray.getString(i));
                                    product_name.add(jsonArray1.getString(i));
                                    category.add(jsonArray2.getString(i));
                                    subcate.add(jsonArray3.getString(i));
                                    price.add(jsonArray4.getString(i));
                                    pic.add(jsonArray5.getString(i));
                                    description.add(jsonArray6.getString(i));
                                    producer.add(jsonArray7.getString(i));
                                }
                                progressDialog.dismiss();
                            }
                            getadapter(pic, productid, product_name, price, category, producer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(prodictlists.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
//                params.put("username",username);
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getadapter(ArrayList<String> pic, ArrayList<String> productid, ArrayList<String> product_name, ArrayList<String> price, ArrayList<String> category, ArrayList<String> producer) {
        customAdapter = new CustomAadapter(prodictlists.this,pic, productid, product_name, price, category, producer,this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(prodictlists.this));
    }

//    void storeDataInArrays(){
//        Cursor cursor = myDB.readAllData();
//        if(cursor.getCount() == 0){
//            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
//            return;
//        }else{
//            while (cursor.moveToNext()){
//                prodpic.add(cursor.getBlob(5));
//                productid.add(cursor.getInt(0));
//                productname.add(cursor.getString(1));
//                catid.add(cursor.getInt(2));
//                category.add(cursor.getString(3));
//                price.add(cursor.getString(4));
//                producer.add(cursor.getString(7));
//                description.add(cursor.getString(6));
//            }
//        }
//    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemUpdate(int position) {
        Intent intent = new Intent(prodictlists.this, productupdate.class);
        intent.putExtra("name",product_name.get(position));
        intent.putExtra("pic",pic.get(position));
        intent.putExtra("id",productid.get(position));
        intent.putExtra("desc",description.get(position));
        intent.putExtra("category",category.get(position));
        intent.putExtra("price",price.get(position));
        intent.putExtra("producer",producer.get(position));
        intent.putExtra("subcate",subcate.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemDelete(int position) {

        progressDialog.setMessage("Updating product...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_DELETEPRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            Toast.makeText(prodictlists.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),prodictlists.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(prodictlists.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("productid",productid.get(position));
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


//        Boolean delete=myDB.deleteData(productid.get(position));
//        if(delete==true){
//            Toast.makeText(this, "The delete was successful", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), prodictlists.class);
//            startActivity(intent);
//        }
//        else {
//            Toast.makeText(this, "The delete was not successful", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onDateAccept(String date) {

    }

    @Override
    public void onDateDecline(String date) {

    }
}