package com.example.negotium.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.negotium.Constants;
import com.example.negotium.CustomAdapter;
import com.example.negotium.CustomCategory;
import com.example.negotium.DatabaseHelper;
import com.example.negotium.R;
import com.example.negotium.RecyclerViewInterface;
import com.example.negotium.RequestHandler;
import com.example.negotium.Subcategory;
import com.example.negotium.databinding.FragmentHomeBinding;
import com.example.negotium.productlook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements RecyclerViewInterface{
    RecyclerView recyclerView,categoryview;
    CustomAdapter customAdapter;
    CustomCategory customCategory;

//    private ArrayList<Integer> cateid;
//    private ArrayList<String> catename;
    private FragmentHomeBinding binding;
//    private ArrayList<byte[]> prodpic;

//    private ArrayList<Integer> productid,catid;
//    private ArrayList<String>   productname, prodesc, price, category, producer;
    ArrayList<String> productid, product_name, category, subcate,price,pic,description,producer,categoryy;
    ArrayList<String> rating;
    DatabaseHelper db;
    private ProgressDialog progressDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());

        productid = new ArrayList<>();
        product_name = new ArrayList<>();
        category = new ArrayList<>();
        subcate = new ArrayList<>();
        price = new ArrayList<>();
        pic = new ArrayList<>();
        description = new ArrayList<>();
        producer = new ArrayList<>();
        categoryy = new ArrayList<>();
        rating = new ArrayList<>();
        lol();
//        db= new DatabaseHelper(getContext());
//        prodpic =new ArrayList<>();
//        productid = new ArrayList<>();
//        productname = new ArrayList<>();
//        prodesc = new ArrayList<>();
//        price = new ArrayList<>();
//        category = new ArrayList<>();
//        producer = new ArrayList<>();
//        catid = new ArrayList<>();
//        cateid = new ArrayList<>();
//        catename = new ArrayList<>();

//        storeDataInArrays();
//        storedataforcata();
        binding.searchView.clearFocus();

        recyclerView=view.findViewById(R.id.recycleview);
        categoryview=view.findViewById(R.id.catagoryview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pic =new ArrayList<>();
                productid = new ArrayList<>();
                product_name = new ArrayList<>();
                description = new ArrayList<>();
                price = new ArrayList<>();
                category = new ArrayList<>();
                producer = new ArrayList<>();
                categoryy = new ArrayList<>();
                rating = new ArrayList<>();
//                catid = new ArrayList<>();
//                cateid = new ArrayList<>();
//                catename = new ArrayList<>();
//                storeDataInArrayss(newText);
                storeArrays(newText);


                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                categoryview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                return true;
            }
        });
    }

//    private void storedataforcata() {
//        Cursor cursor1= db.readallcate();
//        if(cursor1.getCount() == 0){
//            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
//        }else{
//            while (cursor1.moveToNext()){
//                cateid.add(cursor1.getInt(0));
//                catename.add(cursor1.getString(1));
//            }
//        }
//    }

    private void getadapter(/*ArrayList<byte[]> prodpic*/ArrayList<String> pic, ArrayList<String> product_name, ArrayList<String> price, ArrayList<String> categoryy, ArrayList<String> productid,ArrayList<String> producer,ArrayList<String> rating) {
        customCategory = new CustomCategory(getContext(),productid,categoryy,this);
        categoryview.setAdapter(customCategory);
//        categoryview.setLayoutManager(new LinearLayoutManager(getContext()));

        customAdapter = new CustomAdapter(getContext(),pic, product_name, price, producer,rating,this);
        recyclerView.setAdapter(customAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void storeArrays(String search){
        progressDialog.setMessage("showing products...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_PRODUCTSEARCH,
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
                            JSONArray jsonArray8 = (JSONArray)obj.getJSONArray("categoryy");
                            JSONArray jsonArray9 = (JSONArray)obj.getJSONArray("rating");
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
                                    if (jsonArray9.getString(i)=="null") {
                                        rating.add("0");
                                    }
                                    else {
                                        rating.add(jsonArray9.getString(i));
                                    }
                                }
                                for (int i = 0; i < jsonArray8.length(); i++) {
                                    categoryy.add(jsonArray8.getString(i));
                                }
                                progressDialog.dismiss();
                            }}else{
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            getadapter(pic, product_name, price, categoryy,productid,producer,rating);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


//    private void storeDataInArrayss(String search) {
//        Cursor cursor = db.readsomeData(search);
//        if(cursor.getCount() == 0){
//            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
//        }else{
//            while (cursor.moveToNext()){
//                prodpic.add(cursor.getBlob(5));
//                productid.add(cursor.getInt(0));
//                productname.add(cursor.getString(1));
//                category.add(cursor.getString(3));
//                price.add(cursor.getString(4));
//                prodesc.add(cursor.getString(6));
//                producer.add(cursor.getString(7));
//                catid.add(cursor.getInt(2));
//            }
//        }
//        Cursor cursor1= db.readsomecate(search);
//        if(cursor1.getCount() == 0){
//            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
//        }else{
//            while (cursor1.moveToNext()){
//                cateid.add(cursor1.getInt(0));
//                catename.add(cursor1.getString(1));
//            }
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//    void storeDataInArrays(){
//        Cursor cursor = db.readAllData();
//        if(cursor.getCount() == 0){
//            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
//        }else {
//            while (cursor.moveToNext()) {
//                prodpic.add(cursor.getBlob(5));
//                productid.add(cursor.getInt(0));
//                productname.add(cursor.getString(1));
//                category.add(cursor.getString(3));
//                price.add(cursor.getString(4));
//                prodesc.add(cursor.getString(6));
//                producer.add(cursor.getString(7));
//                catid.add(cursor.getInt(2));
//            }
//        }
//    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), productlook.class);
//        intent.putExtra("name",product_name.get(position));
//        intent.putExtra("pic",pic.get(position));
        intent.putExtra("id",productid.get(position));
//        intent.putExtra("desc",description.get(position));
//        intent.putExtra("category",category.get(position));
//        intent.putExtra("price",price.get(position));
//        intent.putExtra("producer",producer.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemUpdate(int position) {
        Intent intent = new Intent(getContext(), Subcategory.class);
//        intent.putExtra("cateid",productid.get(position));
        intent.putExtra("catename",categoryy.get(position));
        startActivity(intent);
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

    private void lol(){
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
                            JSONArray jsonArray8 = (JSONArray)obj.getJSONArray("categoryy");
                            JSONArray jsonArray9 = (JSONArray)obj.getJSONArray("rating");
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
                                    if (jsonArray9.getString(i)=="null") {
                                        rating.add("0");
                                    }
                                    else {
                                        rating.add(jsonArray9.getString(i));
                                    }
                                }
                                for (int i = 0; i < jsonArray8.length(); i++) {
                                    categoryy.add(jsonArray8.getString(i));
                                }
                                progressDialog.dismiss();
                            }
                            getadapter(pic, product_name, price, categoryy,productid,producer,rating);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}