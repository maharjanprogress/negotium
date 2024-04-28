package com.example.negotium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.negotium.databinding.ActivityProductlookBinding;

public class productlook extends AppCompatActivity {
    int id;
    ActivityProductlookBinding binding;
    Bitmap lol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductlookBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        id= getIntent().getIntExtra("id",5);
        String namee = getIntent().getStringExtra("name");
        String priceee = getIntent().getStringExtra("price");
        String descee = getIntent().getStringExtra("desc");
        String categoryee = getIntent().getStringExtra("category");
        String produceree = getIntent().getStringExtra("producer");
        byte[] image = getIntent().getByteArrayExtra("pic");
        lol = BitmapFactory.decodeByteArray(image,0,image.length);
        binding.lookprice.setText(priceee);
        binding.lookpic.setImageBitmap(lol);
        binding.lookname.setText(namee);
        binding.lookdesc.setText(descee);
        binding.lookcategory.setText(categoryee);
        binding.lookproducer.setText(produceree);
    }
}