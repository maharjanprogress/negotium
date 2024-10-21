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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.negotium.databinding.ActivityProductaddBinding;
import com.example.negotium.databinding.ActivitySubcategoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Subcategory extends AppCompatActivity implements RecyclerViewInterface{
    ActivitySubcategoryBinding binding;
    DatabaseHelper mydb;
//    ArrayList<Integer> catid;
    ArrayList<String> pic,subcate;
    CustomSubcategory customSubcategory;
    RecyclerView recyclerView;
    String id,category;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubcategoryBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);

        pic = new ArrayList<>();
        subcate = new ArrayList<>();

        category = getIntent().getStringExtra("catename");
        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setTitle("Subcategory for "+category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        okey(category);
//        id= String.valueOf(getIntent().getIntExtra("cateid",5));
//        mydb = new DatabaseHelper(this);
//        catid = new ArrayList<>();
//        category = new ArrayList<>();
//        storeDataInArray(id);
        recyclerView = findViewById(R.id.subrecycler);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }

    private void okey(String category) {
        progressDialog.setMessage("showing subcategory...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_SUBCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("subcate");
                                JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("pic");
                                if(jsonArray3 != null){
                                    for (int i = 0; i < jsonArray3.length(); i++) {
                                        subcate.add(jsonArray3.getString(i));
                                        pic.add(jsonArray5.getString(i));
                                    }
                                    progressDialog.dismiss();
                                }}else{
                                Toast.makeText(Subcategory.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            getadapter(pic, subcate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Subcategory.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("subcate",category);
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(Subcategory.this).addToRequestQueue(stringRequest);
    }

    private void getadapter(ArrayList<String> pic, ArrayList<String> subcate) {
        customSubcategory = new CustomSubcategory(this,this,subcate,pic);
        recyclerView.setAdapter(customSubcategory);
    }

//    private void storeDataInArray(String id) {
//        Cursor cursor = mydb.getcategory(id);
//        if(cursor.getCount()==0){
//            Toast.makeText(this, "No subcategory found", Toast.LENGTH_SHORT).show();
//        }else{
////            catid.add(cursor.getInt(0));
//            category.add(cursor.getString(0));
//        }
//    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, SubcateProduct.class);
        intent.putExtra("subcate",subcate.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemUpdate(int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }
}