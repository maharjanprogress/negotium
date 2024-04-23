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

import java.util.ArrayList;

public class prodictlists extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper myDB;
    ArrayList<byte[]> prodpic;
    ArrayList<Integer> productid;
    ArrayList<String> productname, prodesc, price, category, producer;

    CustomAdapter customAdapter;

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
        prodesc = new ArrayList<>();
        price = new ArrayList<>();
        category = new ArrayList<>();
        producer = new ArrayList<>();

        storeDataInArrays();
        recyclerView=findViewById(R.id.recyclerView);

        customAdapter = new CustomAdapter(prodictlists.this,prodpic, productid, productname, prodesc, price, category, producer);
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
}