package com.example.negotium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    //private ArrayList<Integer> productid;
    private ArrayList  productname, prodesc, price, category, producer;

    CustomAdapter(Context context,/*ArrayList<Integer> productid,*/ArrayList productname,ArrayList prodesc,ArrayList price,ArrayList category,ArrayList producer){
        this.context= context;
        //this.productid= productid;
        this.productname= productname;
        this.prodesc= prodesc;
        this.price= price;
        this.category= category;
        this.producer= producer;

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
        //holder.productidt.setText(String.valueOf(productid.get(position)));
        holder.productnamet.setText(String.valueOf(productname.get(position)));
        holder.pricet.setText(String.valueOf(price.get(position)));
        holder.categoryt.setText(String.valueOf(category.get(position)));
        holder.producert.setText(String.valueOf(producer.get(position)));
        holder.prodesct.setText(String.valueOf(prodesc.get(position)));
    }

    @Override
    public int getItemCount() {
        return producer.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView /*productidt,*/ productnamet, prodesct, pricet, categoryt, producert;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           // productidt = itemView.findViewById(R.id.prodid);
            productnamet = itemView.findViewById(R.id.prodname);
            prodesct = itemView.findViewById(R.id.prodesc);
            pricet = itemView.findViewById(R.id.price);
            categoryt = itemView.findViewById(R.id.category);
            producert = itemView.findViewById(R.id.producers);
        }
    }
}
