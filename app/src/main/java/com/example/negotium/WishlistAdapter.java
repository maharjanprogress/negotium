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

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.wishlistViewHolder>{
    private List<Wishlist> wishlists;
    private WishlistListener wishlistListenerr;
    private Context context;

    public WishlistAdapter(List<Wishlist> wishlists, WishlistListener wishlistListenerr, Context context) {
        this.wishlists = wishlists;
        this.wishlistListenerr = wishlistListenerr;
        this.context = context;
    }

    @NonNull
    @Override
    public wishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new wishlistViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull wishlistViewHolder holder, int position) {
        holder.bindWishlist(wishlists.get(position));
    }

    @Override
    public int getItemCount() {
        return wishlists.size();
    }

    public List<Wishlist> getSelectedwishlist(){
        List<Wishlist> selectedWishlist = new ArrayList<>();
        for(Wishlist wishlist:wishlists){
            if(wishlist.isSelected){
                selectedWishlist.add(wishlist);
            }
        }
        return selectedWishlist;
    }
    class wishlistViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layoutwishlist;
        View viewBackground;
        RoundedImageView imageWish;
        TextView textName,textquantity,textproducer,textprice;
        ImageView imageSelected;

        public wishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutwishlist = itemView.findViewById(R.id.layoutwishlist);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            imageWish = itemView.findViewById(R.id.imageWish);
            textName = itemView.findViewById(R.id.textName);
            textquantity = itemView.findViewById(R.id.textquantity);
            textproducer = itemView.findViewById(R.id.textproducer);
            textprice = itemView.findViewById(R.id.textprice);
            imageSelected = itemView.findViewById(R.id.imageSelected);
        }

        void bindWishlist(final Wishlist wishlist){
            Glide.with(context).load(Constants.ROOT_IMAGEURL+wishlist.pic).into(imageWish);
            textName.setText(wishlist.name);
            textquantity.setText(wishlist.quantity);
            textproducer.setText(wishlist.producer);
            textprice.setText(wishlist.total_value);
            if(wishlist.isSelected){
                viewBackground.setBackgroundResource(R.drawable.wishlist_selected_background);
                imageSelected.setVisibility(View.VISIBLE);
            }else{
                viewBackground.setBackgroundResource(R.drawable.wishlist_background);
                imageSelected.setVisibility(View.GONE);
            }
            layoutwishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(wishlist.isSelected){
                        viewBackground.setBackgroundResource(R.drawable.wishlist_background);
                        imageSelected.setVisibility(View.GONE);
                        wishlist.isSelected = false;
                        if(getSelectedwishlist().size()==0){
                            wishlistListenerr.onwishlistAction(false);
                        }
                    }else{
                        viewBackground.setBackgroundResource(R.drawable.wishlist_selected_background);
                        imageSelected.setVisibility(View.VISIBLE);
                        wishlist.isSelected = true;
                        wishlistListenerr.onwishlistAction(true);
                    }
                }
            });
            imageWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION){
                        wishlistListenerr.onItemSelect(pos);
                    }
                }
            });
        }


    }
}
