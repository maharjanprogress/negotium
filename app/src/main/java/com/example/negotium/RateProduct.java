package com.example.negotium;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.example.negotium.databinding.ActivityBoughtProductBinding;
import com.example.negotium.databinding.ActivityRateProductBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RateProduct extends AppCompatActivity implements BuylistListener{
    private ProgressDialog progressDialog;
    ActivityRateProductBinding binding;
    RecyclerView recyclerView;
    RatingBar ratingBar;
    Button btnRate;
    EditText desc;
    Dialog dialog;
    Integer userid;
    StarAdapter starAdapter;
    List<BuyList> buyLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRateProductBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userid = SharedPrefManager.getInstance(this).getUserid();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popupstar);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popupbg);
        btnRate=dialog.findViewById(R.id.btnRate);
        desc=dialog.findViewById(R.id.desc);
        ratingBar=dialog.findViewById(R.id.ratingBar);

        progressDialog = new ProgressDialog(this);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        setSupportActionBar(binding.mytoolbar);
        getSupportActionBar().setTitle("RATE YOUR PRODUCTS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buyLists = new ArrayList<>();
        recyclerView = findViewById(R.id.rateProductRecycle);

        progressDialog.setMessage("showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_BUYLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray0 = (JSONArray)obj.getJSONArray("buyid");
                                JSONArray jsonArray = (JSONArray)obj.getJSONArray("productid");
                                JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("product_name");
                                JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("pic");
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("israted");
                                JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("quantity");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("totalvalue");
                                JSONArray jsonArray6 = (JSONArray)obj.getJSONArray("buydate");
                                JSONArray jsonArray7 = (JSONArray)obj.getJSONArray("totaldays");
                                String total = obj.getString("finalvalue");
                                if(jsonArray != null){
                                    buyLists = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        BuyList the = new BuyList();
                                        the.id = jsonArray0.getString(i);
                                        the.productid = jsonArray.getString(i);
                                        the.productname = jsonArray1.getString(i);
                                        the.pic = jsonArray2.getString(i);
                                        the.israted = String.valueOf(jsonArray3.getInt(i));
                                        the.quantity = jsonArray4.getString(i);
                                        the.totalvalue = jsonArray5.getString(i);
                                        the.buydate = jsonArray6.getString(i);
                                        if (Objects.equals(jsonArray7.getString(i), "0")){
                                            the.totaldays = "the same";
                                        }
                                        else {
                                            the.totaldays = jsonArray7.getString(i);
                                        }
                                        the.isbought = "1";
                                        buyLists.add(the);

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
                                    send(buyLists);
                                }progressDialog.dismiss();
                            }else{
                                Toast.makeText(RateProduct.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RateProduct.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",String.valueOf(userid));
                params.put("bought","1");
                params.put("rated","0");
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void send(List<BuyList> buyLists) {
        starAdapter = new StarAdapter(buyLists,this,this);
        recyclerView.setAdapter(starAdapter);
    }

    @Override
    public void onbuylistAction(Boolean isSelected) {

    }

    @Override
    public void onItemClick(int position) {
        String buyid = buyLists.get(position).id;
        String productid = buyLists.get(position).productid;
        dialog.show();
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String star = String.valueOf(ratingBar.getRating());
                String descc = desc.getText().toString();
                progressDialog.setMessage("Rating Product...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, Constants.URL_RATEPRODUCT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                                try {
                                    JSONObject obj = new JSONObject(s);
                                    Toast.makeText(RateProduct.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RateProduct.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("productid",productid);
                        params.put("buyid",buyid);
                        params.put("star",star);
                        params.put("desc",descc);
                        params.put("id", String.valueOf(userid));
//                params.put("password",password);
                        return params;
                    }
                };
                RequestHandler.getInstance(RateProduct.this).addToRequestQueue(stringRequest);

                refresh();

            }
        });
    }

    private void refresh() {
        Intent intent = new Intent(this,RateProduct.class);
        startActivity(intent);
    }
}