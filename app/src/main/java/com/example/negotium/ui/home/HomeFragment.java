package com.example.negotium.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.negotium.CustomAdapter;
import com.example.negotium.DatabaseHelper;
import com.example.negotium.MainActivity;
import com.example.negotium.R;
import com.example.negotium.RecyclerViewInterface;
import com.example.negotium.databinding.FragmentHomeBinding;
import com.example.negotium.prodictlists;
import com.example.negotium.productlook;
import com.example.negotium.productupdate;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecyclerViewInterface{
    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    private FragmentHomeBinding binding;
    private ArrayList<byte[]> prodpic;

    private ArrayList<Integer> productid;
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

        storeDataInArrays();

        recyclerView=view.findViewById(R.id.recycleview);
        customAdapter = new CustomAdapter(getContext(),prodpic, productname, price, category,this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        }else{
            while (cursor.moveToNext()){
                prodpic.add(cursor.getBlob(4));
                productid.add(cursor.getInt(0));
                productname.add(cursor.getString(1));
                category.add(cursor.getString(2));
                price.add(cursor.getString(3));
                prodesc.add(cursor.getString(5));
                producer.add(cursor.getString(6));
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
        startActivity(intent);
    }

    @Override
    public void onItemUpdate(int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }
}