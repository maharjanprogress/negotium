package com.example.negotium;

import android.content.Intent;
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

import java.util.ArrayList;

public class prodictlists extends AppCompatActivity implements RecyclerViewInterface{

    RecyclerView recyclerView;
    DatabaseHelper myDB;
    ArrayList<byte[]> prodpic;
    ArrayList<Integer> productid,catid;
    ArrayList<String> productname, price, category, producer,description;

    CustomAadapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prodictlists);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myDB = new DatabaseHelper(prodictlists.this);
        prodpic = new ArrayList<>();
        productid = new ArrayList<>();
        productname = new ArrayList<>();
        price = new ArrayList<>();
        category = new ArrayList<>();
        producer = new ArrayList<>();
        description = new ArrayList<>();
        catid = new ArrayList<>();

        storeDataInArrays();
        recyclerView=findViewById(R.id.recyclerView);

        customAdapter = new CustomAadapter(prodictlists.this,prodpic, productid, productname, price, category, producer,this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(prodictlists.this));
    }
    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            return;
        }else{
            while (cursor.moveToNext()){
                prodpic.add(cursor.getBlob(5));
                productid.add(cursor.getInt(0));
                productname.add(cursor.getString(1));
                catid.add(cursor.getInt(2));
                category.add(cursor.getString(3));
                price.add(cursor.getString(4));
                producer.add(cursor.getString(7));
                description.add(cursor.getString(6));
            }
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemUpdate(int position) {
        Intent intent = new Intent(prodictlists.this, productupdate.class);
        intent.putExtra("name",productname.get(position));
        intent.putExtra("pic",prodpic.get(position));
        intent.putExtra("id",productid.get(position));
        intent.putExtra("desc",description.get(position));
        intent.putExtra("category",category.get(position));
        intent.putExtra("price",price.get(position));
        intent.putExtra("producer",producer.get(position));
        intent.putExtra("cateid",catid.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemDelete(int position) {
        Boolean delete=myDB.deleteData(productid.get(position));
        if(delete==true){
            Toast.makeText(this, "The delete was successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), prodictlists.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "The delete was not successful", Toast.LENGTH_SHORT).show();
        }
    }
}