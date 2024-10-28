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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuylistAdapter extends RecyclerView.Adapter<BuylistAdapter.buylistViewHolder>{
    private List<BuyList> buyLists;
    private BuylistListener buylistListener;
    private Context context;

    public BuylistAdapter(List<BuyList> buyLists, BuylistListener buylistListener, Context context) {
        this.buyLists = buyLists;
        this.buylistListener = buylistListener;
        this.context = context;
    }

    @NonNull
    @Override
    public buylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new buylistViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.buyitems,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull buylistViewHolder holder, int position) {
        holder.bindbuylist(buyLists.get(position));
    }

    @Override
    public int getItemCount() {
        return buyLists.size();
    }

    public List<BuyList> getSelectedbuylist(){
        List<BuyList> selectedbuylist = new ArrayList<>();
        for(BuyList buylist:buyLists){
            if(buylist.isSelected){
                selectedbuylist.add(buylist);
            }
        }
        return selectedbuylist;
    }

    class buylistViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layoutbuylist;
        View viewBackground;
        ImageView imageBuy,imageSelected;

        TextView buyName,buyquantity,buyprice,buytrace,buydate;

        public buylistViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutbuylist = itemView.findViewById(R.id.layoutbuylist);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            imageBuy = itemView.findViewById(R.id.imageBuy);
            buyName = itemView.findViewById(R.id.buyName);
            buyquantity = itemView.findViewById(R.id.buyquantity);
            buyprice = itemView.findViewById(R.id.buyprice);
            buytrace = itemView.findViewById(R.id.buytrace);
            buydate = itemView.findViewById(R.id.buydate);
            imageSelected = itemView.findViewById(R.id.imageSelected);
        }

        void bindbuylist(final BuyList buyList){
            Glide.with(context).load(Constants.ROOT_IMAGEURL+buyList.pic).into(imageBuy);
            buyName.setText(buyList.productname);
            buyquantity.setText(buyList.quantity);
            buydate.setText("Bought in "+buyList.buydate);
            buyprice.setText(buyList.totalvalue);
            if(buyList.isbought=="0"){
                buytrace.setText("Your product has been placed on the waiting list and is currently pending approval from the shop owner. We appreciate your patience as we work to ensure that each product meets our quality and suitability standards. You will be notified promptly once a decision has been made.");
                if(buyList.isSelected){
                    viewBackground.setBackgroundResource(R.drawable.wishlist_selected_background);
                    imageSelected.setVisibility(View.VISIBLE);
                }else{
                    viewBackground.setBackgroundResource(R.drawable.wishlist_background);
                    imageSelected.setVisibility(View.GONE);
                }
                layoutbuylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(buyList.isSelected){
                            viewBackground.setBackgroundResource(R.drawable.wishlist_background);
                            imageSelected.setVisibility(View.GONE);
                            buyList.isSelected = false;
                            if(getSelectedbuylist().size()==0){
                                buylistListener.onbuylistAction(false);
                            }
                        }else{
                            viewBackground.setBackgroundResource(R.drawable.wishlist_selected_background);
                            imageSelected.setVisibility(View.VISIBLE);
                            buyList.isSelected = true;
                            buylistListener.onbuylistAction(true);
                        }
                    }
                });
            }else{
                if(Objects.equals(buyList.israted, "0")){
                    buytrace.setText("The product was successfully delivered in "+buyList.totaldays+" days, and we hope it has met your expectations. At your convenience, we kindly invite you to provide a rating for the product.");
                } else if (Objects.equals(buyList.israted, "1")) {
                    buytrace.setText("The product was delivered in "+buyList.totaldays+" days, and we appreciate your prompt feedback, as you've already provided a rating. Thank you for helping us improve our service and product offerings.");

                }

            }


        }


    }
}
