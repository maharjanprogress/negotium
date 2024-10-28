package com.example.negotium;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.negotium.databinding.ActivityProductlookBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class productlook extends AppCompatActivity {
    String id;
    Integer userid;
    ActivityProductlookBinding binding;
    Bitmap lol;
    Dialog dialog;
    Button btnplus,btnminus,addwish;
    TextView text;
    Integer buttonflag;
    private ProgressDialog progressDialog;
    private String priceee,descee,categoryee,produceree,picc,subcate,namee,ratinge,counte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductlookBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(this);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userid = SharedPrefManager.getInstance(this).getUserid();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popupbg);
        btnplus = dialog.findViewById(R.id.plus);
        btnminus = dialog.findViewById(R.id.minus);
        addwish = dialog.findViewById(R.id.addWish);

        text = dialog.findViewById(R.id.numquantity);

        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(text.getText().toString());
                text.setText(String.valueOf(temp+1));
            }
        });
        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(text.getText().toString());
                if(temp>1) {
                    text.setText(String.valueOf(temp - 1));
                }
            }
        });

//        id= getIntent().getIntExtra("id",5);
        id= getIntent().getStringExtra("id");
//        String namee = getIntent().getStringExtra("name");
//        String priceee = getIntent().getStringExtra("price");
//        String descee = getIntent().getStringExtra("desc");
//        String categoryee = getIntent().getStringExtra("category");
//        String produceree = getIntent().getStringExtra("producer");
//        String picc = getIntent().getStringExtra("pic");
//        byte[] image = getIntent().getByteArrayExtra("pic");
//        lol = BitmapFactory.decodeByteArray(image,0,image.length);



        getproductdetails(id);

        binding.wishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            
            public void onClick(View v) {
                buttonflag = 1;
                dialog.show();
            }
            
        });

        addwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAPI(userid, id, text.getText().toString(),buttonflag);
            }
        });
        
        binding.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonflag = 2;
                dialog.show();
            }
        });

        binding.ratingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(counte) > 0) {
                    Intent intent = new Intent(productlook.this, ViewProductRating.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else {
                    Toast.makeText(productlook.this, "No reviews yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    private void getproductdetails(String id) {
        progressDialog.setMessage("Getting product details...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_PRODUCTSEARCH,
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
                            JSONArray jsonArray8 = (JSONArray)obj.getJSONArray("rating");
                            JSONArray jsonArray9 = (JSONArray)obj.getJSONArray("count");
                            if(jsonArray1 != null){
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    priceee=jsonArray4.getString(i);
                                    subcate=jsonArray3.getString(i);
                                    descee=jsonArray6.getString(i);
                                    categoryee=jsonArray2.getString(i);
                                    produceree=jsonArray7.getString(i);
                                    picc=jsonArray5.getString(i);
                                    namee=jsonArray1.getString(i);
                                    if (jsonArray8.getString(i)=="null"){
                                        ratinge="0";
                                    }else {
                                        ratinge=jsonArray8.getString(i);
                                    }

                                    counte=jsonArray9.getString(i);
                                }
                                putdetails(namee,priceee,descee,categoryee,produceree,picc,subcate,ratinge,counte);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(productlook.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("productid",id);
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void putdetails(String namee, String priceee, String descee, String categoryee, String produceree, String picc, String subcate, String ratinge, String counte) {
        binding.lookprice.setText(priceee);
//        binding.lookpic.setImageBitmap(lol);
        Glide.with(this).load(Constants.ROOT_IMAGEURL+picc).into(binding.lookpic);
        binding.lookdesc.setText(descee);
        binding.lookcategory.setText(categoryee);
        binding.lookproducer.setText(produceree);
        binding.lookname.setText(namee);
        binding.viewrating.setRating(Float.parseFloat(ratinge));
        binding.reviewnum.setText("View Ratings & Reviews("+counte+")");
    }

    private void sendAPI(Integer userid, String id, String quantity, Integer flag) {
        progressDialog.setMessage("Adding to wishlist...");
        progressDialog.show();
        String URL = null;
        if(flag==1){
            URL=Constants.URL_ADDWISH;
        } else if (flag==2) {
            URL=Constants.URL_BUYDIRECT;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            Toast.makeText(productlook.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(productlook.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("productid",id);
                params.put("id", String.valueOf(userid));
                params.put("quantity",quantity);
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}