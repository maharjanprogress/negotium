package com.example.negotium;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.negotium.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

//    RecyclerView recyclerView;
//    DatabaseHelper myDB;
//
//   // ArrayList<Integer> productid;
//    ArrayList<String>   productname, prodesc, price, category, producer;

//    public CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        myDB = new DatabaseHelper(MainActivity.this);
//       // productid = new ArrayList<Integer>();
//        productname = new ArrayList<>();
//        prodesc = new ArrayList<>();
//        price = new ArrayList<>();
//        category = new ArrayList<>();
//        producer = new ArrayList<>();
//
//        storeDataInArrays();
//
//        customAdapter = new CustomAdapter(MainActivity.this/*, productid*/, productname, prodesc, price, category, producer);
//        //recyclerView.setAdapter(customAdapter);
//        //recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "soon be putting customer care in here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
//    void storeDataInArrays(){
//        Cursor cursor = myDB.readAllData();
//        if(cursor.getCount() == 0){
//            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
//        }else{
//            while (cursor.moveToNext()){
//                //productid.add(cursor.getInt(0));
//                productname.add(cursor.getString(1));
//                category.add(cursor.getString(2));
//                price.add(cursor.getString(3));
//                prodesc.add(cursor.getString(5));
//                producer.add(cursor.getString(6));
//            }
//        }
//    }
}