package com.example.negotium;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.negotium.databinding.ActivityProductupdateBinding;

import java.util.ArrayList;

public class productupdate extends AppCompatActivity implements RecyclerViewInterface{
    int id;
    RecyclerView recyclerView;
    CustomCategory customCategory;
    ActivityProductupdateBinding binding;
    DatabaseHelper databaseHelper;
    ActivityResultLauncher<Intent> resultLauncher;
    Bitmap please;
    ArrayList<Integer> cateid;
    ArrayList<String> catename;
    String cid="0";
    String cname;

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

        databaseHelper = new DatabaseHelper(productupdate.this);
        id= getIntent().getIntExtra("id",5);
        cid = String.valueOf(getIntent().getIntExtra("cateid",3));
        Cursor cursor=databaseHelper.getcatname(cid);
        cursor.moveToFirst();
        cname = cursor.getString(0);
        String namee = getIntent().getStringExtra("name");
        String priceee = getIntent().getStringExtra("price");
        String descee = getIntent().getStringExtra("desc");
        String categoryee = getIntent().getStringExtra("category");
        String produceree = getIntent().getStringExtra("producer");
        byte[] image = getIntent().getByteArrayExtra("pic");
        please = BitmapFactory.decodeByteArray(image,0,image.length);
        binding.searchcate.setQuery(cname,false);
        binding.priceu.setText(priceee);
        binding.imageuploadu.setImageBitmap(please);
        binding.pronameu.setText(namee);
        binding.prodescu.setText(descee);
        binding.categoryu.setText(categoryee);
        binding.produceru.setText(produceree);

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
                cateid= new ArrayList<>();
                catename= new ArrayList<>();
                binding.hiddenname.setText(newText);
                storeDataInArrayss(newText);
                getadapter(cateid,catename);
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
                    //  String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Pictures/"+ binding.filename.getText().toString()+".jpeg";
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.imageupload);
                    if(cid=="0") {
                        Boolean doo = databaseHelper.insertCategory(cname);
                        if (doo == true) {
                            Toast.makeText(productupdate.this, "A new category added", Toast.LENGTH_SHORT).show();
                            Cursor cursor = databaseHelper.getcatid(cname);
                            cursor.moveToFirst();
                            cid = String.valueOf(cursor.getInt(0));
                        } else {
                            Toast.makeText(productupdate.this, "not done", Toast.LENGTH_SHORT).show();
                        }
                        Boolean insert=databaseHelper.updatedetails(please,product_name,producer,description,price,category,id,cid);
                        if(insert==true){
                            Toast.makeText(productupdate.this, "The Update was Successful", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),prodictlists.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(productupdate.this, "Could not update", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Boolean insert=databaseHelper.updatedetails(please,product_name,producer,description,price,category,id,cid);
                        if(insert==true){
                            Toast.makeText(productupdate.this, "The Update was Successful", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),prodictlists.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(productupdate.this, "Could not update", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    private void getadapter(ArrayList<Integer> cateid, ArrayList<String> catename) {
        customCategory = new CustomCategory(this,cateid,catename,this);
        recyclerView.setAdapter(customCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void storeDataInArrayss(String newText) {
        Cursor cursor = databaseHelper.readsomecate(newText);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
            binding.hidden.setText("0");
        }else{
            while (cursor.moveToNext()){
                cateid.add(cursor.getInt(0));
                catename.add(cursor.getString(1));
            }
        }
    }

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
        cid=String.valueOf(cateid.get(position));
        cname=catename.get(position);
        binding.hidden.setText(cid);
        binding.hiddenname.setText(String.valueOf(cname));
        binding.searchcate.setQuery(cname,false);
        recyclerView.setLayoutManager(null);
    }

    @Override
    public void onItemDelete(int position) {

    }
}