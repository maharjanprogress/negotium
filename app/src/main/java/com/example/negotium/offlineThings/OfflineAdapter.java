package com.example.negotium.offlineThings;

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

import com.example.negotium.CustomAdapter;
import com.example.negotium.R;
import com.example.negotium.RecyclerViewInterface;

import java.util.ArrayList;

public class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    private ArrayList prodpic,productname,price, category,description;

    public OfflineAdapter(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList prodpic, ArrayList productname, ArrayList price, ArrayList category, ArrayList description) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.prodpic = prodpic;
        this.productname = productname;
        this.price = price;
        this.category = category;
        this.description = description;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.offlinerow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        byte[] image = (byte[]) prodpic.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        holder.productpic.setImageBitmap(bitmap);
        holder.productname.setText(String.valueOf(productname.get(position)));
        holder.price.setText(String.valueOf(price.get(position)));
        holder.category.setText(String.valueOf(category.get(position)));
        holder.description.setText(String.valueOf(description.get(position)));
    }

    @Override
    public int getItemCount() {
        return productname.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productpic;
        TextView productname, price, category, description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productpic = itemView.findViewById(R.id.prodpicc);
            productname = itemView.findViewById(R.id.prodnamee);
            price = itemView.findViewById(R.id.pricee);
            category = itemView.findViewById(R.id.categorye);
            description = itemView.findViewById(R.id.description);

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
