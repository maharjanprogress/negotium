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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.negotium.databinding.ActivityViewProductRatingBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewProductRating extends AppCompatActivity {

    private ProgressDialog progressDialog;
    String proid;
    ActivityViewProductRatingBinding binding;
    RecyclerView recyclerView;
    RateAdapter rateAdapter;
    List<ProductRateList> rateLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProductRatingBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        proid= getIntent().getStringExtra("id");
        progressDialog = new ProgressDialog(this);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        setSupportActionBar(binding.mytoolbar);
        getSupportActionBar().setTitle("Product Ratings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rateLists = new ArrayList<>();
        recyclerView = findViewById(R.id.rateViewRecycle);

        lol(proid);
//        for (int i = 0; i < 5; i++) {
//            ProductRateList the = new ProductRateList();
//            the.username = "json";
//            the.desc = "lol";
//            the.rating = proid;
//            the.date = "2022/22/22";
//            rateLists.add(the);
//        }
//        send(rateLists);


    }

    private void lol(String proid) {
        progressDialog.setMessage("showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_GETPRODUCTRATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("username");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("desc");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("rating");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("date");
                                if(jsonArray != null){
                                    rateLists = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ProductRateList the = new ProductRateList();
                                        the.username = jsonArray.getString(i);
                                        the.desc = jsonArray1.getString(i);
                                        the.rating = jsonArray2.getString(i);
                                        the.date = jsonArray3.getString(i);
                                        rateLists.add(the);

//                                        productid.add(jsonArray.getString(i));
//                                        product_name.add(jsonArray1.getString(i));
//                                        category.add(jsonArray2.getString(i));
//                                        subcate.add(jsonArray3.getString(i));
//                                        price.add(jsonArray4.getString(i));
//                                        pic.add(jsonArray5.getString(i));
//                                        description.add(jsonArray6.getString(i));
//                                        producer.add(jsonArray7.getString(i));
                                    }
                                    progressDialog.dismiss();
                                    send(rateLists);
                                }progressDialog.dismiss();
                            }else{
                                Toast.makeText(ViewProductRating.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ViewProductRating.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("productid",proid);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void send(List<ProductRateList> rateLists) {
        rateAdapter = new RateAdapter(rateLists,this);
        recyclerView.setAdapter(rateAdapter);
    }
}