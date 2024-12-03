package com.example.negotium.offlineThings;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.negotium.CustomAdapter;
import com.example.negotium.DatabaseHelper;
import com.example.negotium.LoginActivity;
import com.example.negotium.R;
import com.example.negotium.RecyclerViewInterface;
import com.example.negotium.SharedPrefManager;
import com.example.negotium.databinding.ActivityOfflineHomeBinding;
import com.example.negotium.databinding.ActivityProdictlistsBinding;

import java.util.ArrayList;

public class OfflineHome extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    OfflineAdapter offlineAdapter;
    ActivityOfflineHomeBinding binding;
    ArrayList<String> productid, product_name, category, subcate,price,description,producer;
    private ArrayList<byte[]> pic;
    DatabaseHelper db;
    Dialog dialog;
    String proname = "ok";
    Button btnplus,btnminus,addwish;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOfflineHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        String username = SharedPrefManager.getInstance(this).getUsername();
        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setTitle(username+", This is Offline Mode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popupbg);
        btnplus = dialog.findViewById(R.id.plus);
        btnminus = dialog.findViewById(R.id.minus);
        addwish = dialog.findViewById(R.id.addWish);

        text = dialog.findViewById(R.id.numquantity);

        productid = new ArrayList<>();
        product_name = new ArrayList<>();
        category = new ArrayList<>();
        subcate = new ArrayList<>();
        price = new ArrayList<>();
        pic = new ArrayList<>();
        description = new ArrayList<>();
        producer = new ArrayList<>();
        db= new DatabaseHelper(this);
        storeDataInArrays();

        binding.searchViewoff.clearFocus();
        recyclerView=findViewById(R.id.recycleviewoff);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        offlineAdapter = new OfflineAdapter(this,this,pic, product_name, price, category,description);
        recyclerView.setAdapter(offlineAdapter);
        binding.searchViewoff.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                description = new ArrayList<>();
                storeDataInArrayss(newText);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                getadapter(pic,product_name,description,price,category,description);
                return true;
            }
        });

        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(text.getText().toString());
                text.setText(String.valueOf(temp+1));
            }
        });
        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(text.getText().toString());
                if(temp>1) {
                    text.setText(String.valueOf(temp - 1));
                }
            }
        });
        addwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(OfflineHome.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                    sendSMS(text.getText().toString());
                }else {
                    ActivityCompat.requestPermissions(OfflineHome.this,new String[]{Manifest.permission.SEND_SMS},
                            100);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendSMS(text.getText().toString());
        }else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String string) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("9823831816",null,"Username: "+SharedPrefManager.getInstance(OfflineHome.this).getUsername()+"\nProduct: "+proname+"\nQuantity: "+string,null,null);
        Toast.makeText(this, "message sent sucessfully", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    private void getadapter(ArrayList<byte[]> pic, ArrayList<String> productName, ArrayList<String> description, ArrayList<String> price, ArrayList<String> category, ArrayList<String> description1) {
        offlineAdapter = new OfflineAdapter(this,this,pic, product_name, price, category,description);
        recyclerView.setAdapter(offlineAdapter);
    }

    private void storeDataInArrays() {
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                pic.add(cursor.getBlob(5));
                product_name.add(cursor.getString(1));
                subcate.add(cursor.getString(3));
                price.add(cursor.getString(4));
                description.add(cursor.getString(6));
                producer.add(cursor.getString(7));
                category.add(cursor.getString(2));
            }
        }
    }
    private void storeDataInArrayss(String search) {
        Cursor cursor = db.readsomeData(search);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                pic.add(cursor.getBlob(5));
                product_name.add(cursor.getString(1));
                subcate.add(cursor.getString(3));
                price.add(cursor.getString(4));
                description.add(cursor.getString(6));
                producer.add(cursor.getString(7));
                category.add(cursor.getString(2));
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        proname = product_name.get(position);
        dialog.show();

    }

    @Override
    public void onItemUpdate(int position) {

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