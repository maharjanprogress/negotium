package com.example.negotium;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    //private ArrayList<Integer> productid;
    private ArrayList prodpic,productname,price, category;
    private ArrayList<String> rating;

    public CustomAdapter(Context context, ArrayList prodpic, ArrayList productname, ArrayList price, ArrayList category, ArrayList rating, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.prodpic = prodpic;
//        this.productid = productid;
        this.productname = productname;
//        this.prodesc = prodesc;
        this.price = price;
        this.category = category;
        this.rating=rating;
//        this.producer = producer;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
//        byte[] image = (byte[]) prodpic.get(position);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
//        holder.productpic.setImageBitmap(bitmap);
        Glide.with(context).load(Constants.ROOT_IMAGEURL+prodpic.get(position)).into(holder.productpic);
//        holder.productpic.setImageBitmap(getBitmapFromURL((String) prodpic.get(position)));
        holder.productname.setText(String.valueOf(productname.get(position)));
        holder.price.setText(String.valueOf(price.get(position)));
        holder.category.setText(String.valueOf(category.get(position)));
        holder.ratingBar.setRating(Float.valueOf(rating.get(position)));
    }

    @Override
    public int getItemCount() {
        return prodpic.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productpic;
        TextView productname, price, category;
        RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productpic = itemView.findViewById(R.id.prodpicc);
            productname = itemView.findViewById(R.id.prodnamee);
            price = itemView.findViewById(R.id.pricee);
            category = itemView.findViewById(R.id.categorye);
            ratingBar = itemView.findViewById(R.id.ratingBar2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            Log.e("src",src);
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Log.e("Bitmap","returned");
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("Exception",e.getMessage());
//            return null;
//        }
//    }
}
