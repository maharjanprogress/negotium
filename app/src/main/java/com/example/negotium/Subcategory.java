package com.example.negotium;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.negotium.databinding.ActivityProductaddBinding;
import com.example.negotium.databinding.ActivitySubcategoryBinding;

import java.util.ArrayList;

public class Subcategory extends AppCompatActivity implements RecyclerViewInterface{
    ActivitySubcategoryBinding binding;
    DatabaseHelper mydb;
//    ArrayList<Integer> catid;
    ArrayList<String> category;
    CustomSubcategory customSubcategory;
    RecyclerView recyclerView;
    String id;

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
        String catename = getIntent().getStringExtra("catename");
        id= String.valueOf(getIntent().getIntExtra("cateid",5));
        binding.textView15.setText("Sub Category for "+catename);
        mydb = new DatabaseHelper(this);
//        catid = new ArrayList<>();
        category = new ArrayList<>();
        storeDataInArray(id);
        recyclerView = findViewById(R.id.subrecycler);
        customSubcategory = new CustomSubcategory(this,this,category);
        recyclerView.setAdapter(customSubcategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void storeDataInArray(String id) {
        Cursor cursor = mydb.getcategory(id);
        if(cursor.getCount()==0){
            Toast.makeText(this, "No subcategory found", Toast.LENGTH_SHORT).show();
        }else{
//            catid.add(cursor.getInt(0));
            category.add(cursor.getString(0));
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemUpdate(int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }
}