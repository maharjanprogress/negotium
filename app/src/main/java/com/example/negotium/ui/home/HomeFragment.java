package com.example.negotium.ui.home;

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

import com.example.negotium.CustomAdapter;
import com.example.negotium.CustomCategory;
import com.example.negotium.DatabaseHelper;
import com.example.negotium.R;
import com.example.negotium.RecyclerViewInterface;
import com.example.negotium.Subcategory;
import com.example.negotium.databinding.FragmentHomeBinding;
import com.example.negotium.productlook;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecyclerViewInterface{
    RecyclerView recyclerView,categoryview;
    CustomAdapter customAdapter;
    CustomCategory customCategory;

    private ArrayList<Integer> cateid;
    private ArrayList<String> catename;
    private FragmentHomeBinding binding;
    private ArrayList<byte[]> prodpic;

    private ArrayList<Integer> productid,catid;
    private ArrayList<String>   productname, prodesc, price, category, producer;
    DatabaseHelper db;
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
        db= new DatabaseHelper(getContext());
        prodpic =new ArrayList<>();
        productid = new ArrayList<>();
        productname = new ArrayList<>();
        prodesc = new ArrayList<>();
        price = new ArrayList<>();
        category = new ArrayList<>();
        producer = new ArrayList<>();
        catid = new ArrayList<>();
        cateid = new ArrayList<>();
        catename = new ArrayList<>();

        storeDataInArrays();
        storedataforcata();
        binding.searchView.clearFocus();

        recyclerView=view.findViewById(R.id.recycleview);
        customAdapter = new CustomAdapter(getContext(),prodpic, productname, price, category,this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryview=view.findViewById(R.id.catagoryview);
        customCategory = new CustomCategory(getContext(),cateid,catename,this);
        categoryview.setAdapter(customCategory);
        categoryview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                prodpic =new ArrayList<>();
                productid = new ArrayList<>();
                productname = new ArrayList<>();
                prodesc = new ArrayList<>();
                price = new ArrayList<>();
                category = new ArrayList<>();
                producer = new ArrayList<>();
                catid = new ArrayList<>();
                cateid = new ArrayList<>();
                catename = new ArrayList<>();
                storeDataInArrayss(newText);

                getadapter(prodpic, productname, price, category,cateid,catename);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                categoryview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                return true;
            }
        });
    }

    private void storedataforcata() {
        Cursor cursor1= db.readallcate();
        if(cursor1.getCount() == 0){
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor1.moveToNext()){
                cateid.add(cursor1.getInt(0));
                catename.add(cursor1.getString(1));
            }
        }
    }

    private void getadapter(ArrayList<byte[]> prodpic, ArrayList<String> productname, ArrayList<String> price, ArrayList<String> category, ArrayList<Integer> cateid, ArrayList<String> catename) {
        customCategory = new CustomCategory(getContext(),cateid,catename,this);
        categoryview.setAdapter(customCategory);
        categoryview.setLayoutManager(new LinearLayoutManager(getContext()));

        customAdapter = new CustomAdapter(getContext(),prodpic, productname, price, category,this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void storeDataInArrayss(String search) {
        Cursor cursor = db.readsomeData(search);
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                prodpic.add(cursor.getBlob(5));
                productid.add(cursor.getInt(0));
                productname.add(cursor.getString(1));
                category.add(cursor.getString(3));
                price.add(cursor.getString(4));
                prodesc.add(cursor.getString(6));
                producer.add(cursor.getString(7));
                catid.add(cursor.getInt(2));
            }
        }
        Cursor cursor1= db.readsomecate(search);
        if(cursor1.getCount() == 0){
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor1.moveToNext()){
                cateid.add(cursor1.getInt(0));
                catename.add(cursor1.getString(1));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    void storeDataInArrays(){
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()) {
                prodpic.add(cursor.getBlob(5));
                productid.add(cursor.getInt(0));
                productname.add(cursor.getString(1));
                category.add(cursor.getString(3));
                price.add(cursor.getString(4));
                prodesc.add(cursor.getString(6));
                producer.add(cursor.getString(7));
                catid.add(cursor.getInt(2));
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), productlook.class);
        intent.putExtra("name",productname.get(position));
        intent.putExtra("pic",prodpic.get(position));
        intent.putExtra("id",productid.get(position));
        intent.putExtra("desc",prodesc.get(position));
        intent.putExtra("category",category.get(position));
        intent.putExtra("price",price.get(position));
        intent.putExtra("producer",producer.get(position));
        intent.putExtra("catid",catid.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemUpdate(int position) {
        Intent intent = new Intent(getContext(), Subcategory.class);
        intent.putExtra("cateid",cateid.get(position));
        intent.putExtra("catename",catename.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemDelete(int position) {

    }
}