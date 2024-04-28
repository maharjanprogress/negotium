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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    //private ArrayList<Integer> productid;
    private ArrayList prodpic,productname,price, category;

    public CustomAdapter(Context context, ArrayList prodpic, ArrayList productname, ArrayList price, ArrayList category,RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.prodpic = prodpic;
//        this.productid = productid;
        this.productname = productname;
//        this.prodesc = prodesc;
        this.price = price;
        this.category = category;
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
        byte[] image = (byte[]) prodpic.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        holder.productpic.setImageBitmap(bitmap);
        holder.productname.setText(String.valueOf(productname.get(position)));
        holder.price.setText(String.valueOf(price.get(position)));
        holder.category.setText(String.valueOf(category.get(position)));
    }

    @Override
    public int getItemCount() {
        return prodpic.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productpic;
        TextView productname, price, category;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productpic = itemView.findViewById(R.id.prodpicc);
            productname = itemView.findViewById(R.id.prodnamee);
            price = itemView.findViewById(R.id.pricee);
            category = itemView.findViewById(R.id.categorye);

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
}
