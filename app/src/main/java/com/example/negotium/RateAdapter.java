package com.example.negotium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ratelistViewholder>{
    private List<ProductRateList> rateLists;
    private Context context;

    public RateAdapter(List<ProductRateList> rateLists, Context context) {
        this.rateLists = rateLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ratelistViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ratelistViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewblock,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ratelistViewholder holder, int position) {
        holder.bindratelist(rateLists.get(position));
    }

    @Override
    public int getItemCount() {
        return rateLists.size();
    }

    class ratelistViewholder extends RecyclerView.ViewHolder{
        RatingBar ratingbar;
        TextView username,date,desc;
        public ratelistViewholder(@NonNull View itemView) {
            super(itemView);
            ratingbar = itemView.findViewById(R.id.rrating);
            username = itemView.findViewById(R.id.rusername);
            date = itemView.findViewById(R.id.rdate);
            desc = itemView.findViewById(R.id.rdesc);
        }

        void bindratelist(final ProductRateList rateList){
            username.setText(rateList.username);
            date.setText(rateList.date);
            desc.setText(rateList.desc);
            ratingbar.setRating(Float.parseFloat(rateList.rating));
        }
    }
}
