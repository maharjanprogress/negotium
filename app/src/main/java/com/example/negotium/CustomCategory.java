package com.example.negotium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomCategory extends RecyclerView.Adapter<CustomCategory.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    //private ArrayList<Integer> productid;
    private ArrayList cateid,catename;

    public CustomCategory(Context context, ArrayList cateid, ArrayList catename, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.cateid = cateid;
        this.catename = catename;
    }

    @NonNull
    @Override
    public CustomCategory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.categorynames,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCategory.MyViewHolder holder, int position) {
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
            categorynames= itemView.findViewById(R.id.categorynames);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemUpdate(pos);
                        }
                    }
                }
            });
        }
    }
}
