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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.negotium.R;
import com.example.negotium.databinding.ActivitySubcateProductBinding;
import com.example.negotium.databinding.ActivitySubcategoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubcateProduct extends AppCompatActivity implements RecyclerViewInterface{
    ActivitySubcateProductBinding binding;
    private ProgressDialog progressDialog;
    ArrayList<String> productid, product_name, category, subcate,price,pic,description,producer;
    ArrayList<String> rating;
    CustomAdapter customAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubcateProductBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setTitle("Subcategory Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);

        productid = new ArrayList<>();
        product_name = new ArrayList<>();
        category = new ArrayList<>();
        subcate = new ArrayList<>();
        price = new ArrayList<>();
        pic = new ArrayList<>();
        description = new ArrayList<>();
        producer = new ArrayList<>();
        rating = new ArrayList<>();

        String search = getIntent().getStringExtra("subcate");

        okey(search);

        recyclerView = findViewById(R.id.ProductView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }

    private void okey(String search) {
        progressDialog.setMessage("showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_SUBCATEPRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("productid");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("product_name");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("category");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("subcate");
                                JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("price");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("pic");
                                JSONArray jsonArray6 = (JSONArray)obj.getJSONArray("description");
                                JSONArray jsonArray7 = (JSONArray)obj.getJSONArray("producer");
                                JSONArray jsonArray8 = (JSONArray)obj.getJSONArray("rating");
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
                                        if (jsonArray8.getString(i)=="null") {
                                            rating.add("0");
                                        }
                                        else {
                                            rating.add(jsonArray8.getString(i));
                                        }
                                    }
                                    progressDialog.dismiss();
                                }}else{
                                Toast.makeText(SubcateProduct.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            getadapter(pic, product_name, price,productid,producer,rating);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SubcateProduct.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("search",search);
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getadapter(ArrayList<String> pic, ArrayList<String> product_name, ArrayList<String> price, ArrayList<String> productid, ArrayList<String> producer, ArrayList<String> rating) {
        customAdapter = new CustomAdapter(this,pic, product_name, price, producer,rating,this);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, productlook.class);
        intent.putExtra("name",product_name.get(position));
        intent.putExtra("pic",pic.get(position));
        intent.putExtra("id",productid.get(position));
        intent.putExtra("desc",description.get(position));
        intent.putExtra("category",category.get(position));
        intent.putExtra("price",price.get(position));
        intent.putExtra("producer",producer.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemUpdate(int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }
}