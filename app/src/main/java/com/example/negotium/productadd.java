package com.example.negotium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.negotium.databinding.ActivityProductaddBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class productadd extends AppCompatActivity implements RecyclerViewInterface{
    ActivityProductaddBinding binding;
    RecyclerView recyclerView;
    CustomCategory customCategory;
    DatabaseHelper databaseHelper;
    ArrayList<Integer> cateid;
    ArrayList<String> catename;
    ArrayList<String> productid,category;
    ActivityResultLauncher<Intent> resultLauncher;
    Bitmap please;
    String cid="0";
    String cname;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductaddBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);

        binding.hidden.setText(cid);
        registerResult();
        databaseHelper = new DatabaseHelper(this);
        recyclerView=findViewById(R.id.reciclerview);

        binding.imageupload.setOnClickListener(v -> pickImage());
        binding.searchcate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                cateid= new ArrayList<>();
//                catename= new ArrayList<>();
                category= new ArrayList<>();
                productid= new ArrayList<>();
                binding.hiddenname.setText(newText);
                storeArrays(newText);
//                storeDataInArrayss(newText);
//                getadapter(cateid,catename);
                return true;
            }
        });

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product_name = binding.proname.getText().toString();
                String subcate = binding.category.getText().toString();
                String price = binding.price.getText().toString();
                String description = binding.prodesc.getText().toString();
                String producer = binding.producer.getText().toString();
                cname = binding.hiddenname.getText().toString();

                if(product_name.isEmpty() || price.isEmpty() || producer.isEmpty()){
                    Toast.makeText(productadd.this, "Dont leave the compulsory descriptions of the product", Toast.LENGTH_SHORT).show();
                }
                else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    please.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] imageBytes = baos.toByteArray();
                    String lic = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
                    String pic = Base64.getEncoder().encodeToString(imageBytes);

                    heyy(product_name,cname,subcate,price,pic,description,producer);





                  //  String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Pictures/"+ binding.filename.getText().toString()+".jpeg";
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.imageupload);

//                    if(cid=="0"){
//                        Boolean doo=databaseHelper.insertCategory(cname);
//                        if(doo==true){
//                            Toast.makeText(productadd.this, "A new category added", Toast.LENGTH_SHORT).show();
//                            Cursor cursor= databaseHelper.getcatid(cname);
//                            cursor.moveToFirst();
//                            cid = String.valueOf(cursor.getInt(0));
//                        }
//                        else{
//                            Toast.makeText(productadd.this, "not done", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            Toast.makeText(productadd.this, "no image", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else {
//                        Boolean insert=databaseHelper.insertDetails(please,product_name,producer,description,price,category,cid);
//                        if(insert==true){
//                            Toast.makeText(productadd.this, "the image is sucessfully inserted", Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(getApplicationContext(),productadd.class);
//                            startActivity(intent);
//                        }
//                        else {
//                            Toast.makeText(productadd.this, "no image", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                }
            }
        });
    }

    private void heyy(String product_name, String cname, String subcate, String price, String pic, String description, String producer) {
        progressDialog.setMessage("Inserting product...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_PUTPRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            Toast.makeText(productadd.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),AdminPage.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(productadd.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("product_name",product_name);
                params.put("category",cname);
                params.put("subcate",subcate);
                params.put("price",price);
                params.put("pic",pic);
                params.put("description",description);
                params.put("producer",producer);
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void storeArrays(String newText) {
        progressDialog.setMessage("showing category...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_PRODUCTSEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray8 = (JSONArray)obj.getJSONArray("categoryy");
                                if(jsonArray8 != null){
                                    for (int i = 0; i < jsonArray8.length(); i++) {
                                        category.add(jsonArray8.getString(i));
                                        productid.add(jsonArray8.getString(i));
                                    }
                                    progressDialog.dismiss();
                                }}else{
                                Toast.makeText(productadd.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            getadapter(productid,category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(productadd.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("search",newText);
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getadapter(ArrayList<String> cateid, ArrayList<String> catename) {
        customCategory = new CustomCategory(this,cateid,catename,this);
        recyclerView.setAdapter(customCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

//    public void storeDataInArrayss(String newText) {
//        Cursor cursor = databaseHelper.readsomecate(newText);
//        if(cursor.getCount() == 0){
//            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
//            binding.hidden.setText("0");
//        }else{
//            while (cursor.moveToNext()){
//                cateid.add(cursor.getInt(0));
//                catename.add(cursor.getString(1));
//            }
//        }
//    }



    private void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }
    private void registerResult(){
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    Uri imageuri = result.getData().getData();
                    please=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imageuri);
                    binding.imageupload.setImageURI(imageuri);


                }catch (Exception e){
                    Toast.makeText(productadd.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemUpdate(int position) {
        cid=String.valueOf(productid.get(position));
        cname=category.get(position);
        binding.hidden.setText(cid);
        binding.hiddenname.setText(String.valueOf(cname));
        binding.searchcate.setQuery(cname,false);
        recyclerView.setLayoutManager(null);
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