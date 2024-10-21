package com.example.negotium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomSubcategory extends RecyclerView.Adapter<CustomSubcategory.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList catename,pic;

    public CustomSubcategory(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList catename, ArrayList pic) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.catename = catename;
        this.pic = pic;
    }

    @NonNull
    @Override
    public CustomSubcategory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.subcatagory,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomSubcategory.MyViewHolder holder, int position) {
        Glide.with(context).load(Constants.ROOT_IMAGEURL+pic.get(position)).into(holder.pic);
        holder.categorynames.setText(String.valueOf(catename.get(position)));
    }

    @Override
    public int getItemCount() {
        return catename.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView categorynames;
        ImageView pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categorynames = itemView.findViewById(R.id.categorynames);
            pic = itemView.findViewById(R.id.subcategoryImage);

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
