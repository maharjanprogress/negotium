package com.example.negotium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomSubcategory extends RecyclerView.Adapter<CustomSubcategory.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList catename;

    public CustomSubcategory(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList catename) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.catename = catename;
    }

    @NonNull
    @Override
    public CustomSubcategory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.categorynames,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomSubcategory.MyViewHolder holder, int position) {
        holder.categorynames.setText(String.valueOf(catename.get(position)));
    }

    @Override
    public int getItemCount() {
        return catename.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView categorynames;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categorynames = itemView.findViewById(R.id.categorynames);
        }
    }
}
