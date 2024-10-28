package com.example.negotium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.starlistViewHolder>{
    private List<BuyList> buyLists;
    private BuylistListener buylistListener;
    private Context context;

    public StarAdapter(List<BuyList> buyLists, BuylistListener buylistListener, Context context) {
        this.buyLists = buyLists;
        this.buylistListener = buylistListener;
        this.context = context;
    }

    @NonNull
    @Override
    public starlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new starlistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.staritems,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull starlistViewHolder holder, int position) {
        holder.bindstarlist(buyLists.get(position));

    }

    @Override
    public int getItemCount() {
        return buyLists.size();
    }


    class starlistViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layoutbuylist;
        View viewBackground;
        ImageView imageBuy;

        TextView buyName,buyquantity,buyprice,buytrace,buydate;

        public starlistViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutbuylist = itemView.findViewById(R.id.layoutstarlist);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            imageBuy = itemView.findViewById(R.id.imageStar);
            buyName = itemView.findViewById(R.id.starName);
            buyquantity = itemView.findViewById(R.id.starquantity);
            buyprice = itemView.findViewById(R.id.starprice);
            buytrace = itemView.findViewById(R.id.startrace);
            buydate = itemView.findViewById(R.id.stardate);


            layoutbuylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(buylistListener!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            buylistListener.onItemClick(pos);
                        }
                    }
                }
            });
        }

        void bindstarlist(final BuyList buyList){
            Glide.with(context).load(Constants.ROOT_IMAGEURL+buyList.pic).into(imageBuy);
            buyName.setText(buyList.productname);
            buyquantity.setText(buyList.quantity);
            buydate.setText("Bought in "+buyList.buydate);
            buyprice.setText(buyList.totalvalue);

            buytrace.setText("The product was delivered in "+buyList.totaldays+" days, and we appreciate your prompt feedback, as you've already provided a rating. Thank you for helping us improve our service and product offerings.");
        }



    }
}
