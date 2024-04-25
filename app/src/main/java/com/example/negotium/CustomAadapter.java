package com.example.negotium;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAadapter extends RecyclerView.Adapter<CustomAadapter.MyViewHolder> {
    private Context context;
    private ArrayList prodpic,productid,productname, price, category, producer;

    public CustomAadapter(Context context, ArrayList prodpic, ArrayList productid, ArrayList productname, ArrayList price, ArrayList category, ArrayList producer) {
        this.context = context;
        this.prodpic = prodpic;
        this.productid = productid;
        this.productname = productname;
        this.price = price;
        this.category = category;
        this.producer = producer;
    }

    @NonNull
    @Override
    public CustomAadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_rowadmin,parent,false);
        return new CustomAadapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAadapter.MyViewHolder holder, int position) {
        byte[] image = (byte[]) prodpic.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        holder.productpic.setImageBitmap(bitmap);
        holder.productid.setText(String.valueOf(productid.get(position)));
        holder.productname.setText(String.valueOf(productname.get(position)));
        holder.price.setText(String.valueOf(price.get(position)));
        holder.category.setText(String.valueOf(category.get(position)));
        holder.producer.setText(String.valueOf(producer.get(position)));
    }

    @Override
    public int getItemCount() {
        return producer.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productpic;
        TextView productid, productname, price, category, producer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productpic = itemView.findViewById(R.id.prodpicce);
            productid = itemView.findViewById(R.id.prodide);
            productname = itemView.findViewById(R.id.prodnameee);
            price = itemView.findViewById(R.id.priceee);
            category = itemView.findViewById(R.id.categoryee);
            producer = itemView.findViewById(R.id.producersee);
        }
    }
}
