package com.example.negotium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.negotium.databinding.ActivityProductupdateBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class productupdate extends AppCompatActivity implements RecyclerViewInterface{
    String productid;
    RecyclerView recyclerView;
    CustomCategory customCategory;
    ActivityProductupdateBinding binding;
    DatabaseHelper databaseHelper;
    ActivityResultLauncher<Intent> resultLauncher;
    Bitmap please;
    ArrayList<Integer> cateid;
    ArrayList<String> catename;
    ArrayList<String> productiid,category;
    String cid="0";
    String cname;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductupdateBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);

        databaseHelper = new DatabaseHelper(productupdate.this);

        productid= getIntent().getStringExtra("id");
        cid = getIntent().getStringExtra("subcate");

//        Cursor cursor=databaseHelper.getcatname(cid);
//        cursor.moveToFirst();
//        cname = cursor.getString(0);
        cname = getIntent().getStringExtra("category");
        String namee = getIntent().getStringExtra("name");
        String priceee = getIntent().getStringExtra("price");
        String descee = getIntent().getStringExtra("desc");
        String categoryee = getIntent().getStringExtra("subcate");
        String produceree = getIntent().getStringExtra("producer");
        String pic = getIntent().getStringExtra("pic");
//        byte[] image = getIntent().getByteArrayExtra("pic");
//        please = BitmapFactory.decodeByteArray(image,0,image.length);
//        binding.imageuploadu.setImageBitmap(please);

//        byte[] decodedString = Base64.getDecoder().decode(pic);
//        please = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
//        binding.imageuploadu.buildDrawingCache();
//        please = binding.imageuploadu.getDrawingCache();

        Glide.with(this).load(Constants.ROOT_IMAGEURL+pic).into(binding.imageuploadu);

        Glide.with(this).asBitmap().load(Constants.ROOT_IMAGEURL+pic).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                binding.imageuploadu.setImageBitmap(resource);
                please = resource;
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        binding.searchcate.setQuery(cname,false);
        binding.priceu.setText(priceee);
        binding.pronameu.setText(namee);
        binding.prodescu.setText(descee);
        binding.categoryu.setText(categoryee);
        binding.produceru.setText(produceree);
        binding.hiddenname.setText(cname);

//        Bitmap bitmap = BitmapFactory.decodeByteArray(needpic,0,needpic.length);
//        binding.imageuploadu.setImageBitmap(bitmap);
//        binding.pronameu.setText(prodictlists.passname);

        registerResult();
        recyclerView=findViewById(R.id.reciclerview);
        binding.imageuploadu.setOnClickListener(v -> pickImage());

        binding.searchcate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                cateid= new ArrayList<>();
//                catename= new ArrayList<>();
                binding.hiddenname.setText(newText);
                category= new ArrayList<>();
                productiid= new ArrayList<>();
                storeArrays(newText);
//                storeDataInArrayss(newText);
//                getadapter(cateid,catename);
                return true;
            }
        });
        binding.btnsubmitu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product_name = binding.pronameu.getText().toString();
                String category = binding.categoryu.getText().toString();
                String price = binding.priceu.getText().toString();
                String description = binding.prodescu.getText().toString();
                String producer = binding.produceru.getText().toString();
                cname = binding.hiddenname.getText().toString();

                if(product_name.isEmpty() || price.isEmpty() || producer.isEmpty()){
                    Toast.makeText(productupdate.this, "Dont leave the compulsory descriptions of the product", Toast.LENGTH_SHORT).show();
                }
                else {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    please.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] imageBytes = baos.toByteArray();
                    String pic = Base64.getEncoder().encodeToString(imageBytes);

                    heyy(productid,product_name,cname,category,price,pic,description,producer);

                    //  String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Pictures/"+ binding.filename.getText().toString()+".jpeg";
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.imageupload);
//                    if(cid=="0") {
//                        Boolean doo = databaseHelper.insertCategory(cname);
//                        if (doo == true) {
//                            Toast.makeText(productupdate.this, "A new category added", Toast.LENGTH_SHORT).show();
//                            Cursor cursor = databaseHelper.getcatid(cname);
//                            cursor.moveToFirst();
//                            cid = String.valueOf(cursor.getInt(0));
//                        } else {
//                            Toast.makeText(productupdate.this, "not done", Toast.LENGTH_SHORT).show();
//                        }
//                        Boolean insert=databaseHelper.updatedetails(please,product_name,producer,description,price,category,id,cid);
//                        if(insert==true){
//                            Toast.makeText(productupdate.this, "The Update was Successful", Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(getApplicationContext(),prodictlists.class);
//                            startActivity(intent);
//                        }
//                        else {
//                            Toast.makeText(productupdate.this, "Could not update", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else {
//                        Boolean insert=databaseHelper.updatedetails(please,product_name,producer,description,price,category,id,cid);
//                        if(insert==true){
//                            Toast.makeText(productupdate.this, "The Update was Successful", Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(getApplicationContext(),prodictlists.class);
//                            startActivity(intent);
//                        }
//                        else {
//                            Toast.makeText(productupdate.this, "Could not update", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                }
            }
        });

    }

    private void heyy(String productid,String productName, String cname, String subcate, String price, String pic, String description, String producer) {
        progressDialog.setMessage("Updating product...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_UPDATEPRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            Toast.makeText(productupdate.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(productupdate.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("productid",productid);
                params.put("product_name",productName);
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
                                        productiid.add(jsonArray8.getString(i));
                                    }
                                    progressDialog.dismiss();
                                }}else{
                                Toast.makeText(productupdate.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            getadapter(productiid,category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(productupdate.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    private void storeDataInArrayss(String newText) {
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
                    binding.imageuploadu.setImageURI(imageuri);
                }catch (Exception e){
                    Toast.makeText(productupdate.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemUpdate(int position) {
        cid=String.valueOf(productiid.get(position));
        cname=category.get(position);
        binding.hidden.setText(cid);
        binding.hiddenname.setText(String.valueOf(cname));
        binding.searchcate.setQuery(cname,false);
        recyclerView.setLayoutManager(null);
    }

    @Override
    public void onItemDelete(int position) {

    }
}