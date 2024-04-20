package com.example.negotium;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
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

import com.example.negotium.databinding.ActivityProductaddBinding;

import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;

public class productadd extends AppCompatActivity {
    ActivityProductaddBinding binding;
    DatabaseHelper databaseHelper;
    ActivityResultLauncher<Intent> resultLauncher;
    Bitmap please;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductaddBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerResult();
        databaseHelper = new DatabaseHelper(this);

        binding.imageupload.setOnClickListener(v -> pickImage());

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product_name = binding.proname.getText().toString();
                String category = binding.category.getText().toString();
                String price = binding.price.getText().toString();
                String description = binding.prodesc.getText().toString();
                String producer = binding.producer.getText().toString();

                if(product_name.isEmpty() || price.isEmpty() || producer.isEmpty()){
                    Toast.makeText(productadd.this, "Dont leave the compulsory descriptions of the product", Toast.LENGTH_SHORT).show();
                }
                else {
                  //  String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Pictures/"+ binding.filename.getText().toString()+".jpeg";
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.imageupload);
                    Boolean insert=databaseHelper.insertDetails(please,product_name,producer,description,price,category);
                    if(insert==true){
                        Toast.makeText(productadd.this, "the image is sucessfully inserted", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),productadd.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(productadd.this, "no image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
                    binding.imageupload.setImageURI(imageuri);
                }catch (Exception e){
                    Toast.makeText(productadd.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}