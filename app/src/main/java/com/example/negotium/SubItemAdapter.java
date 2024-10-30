package com.example.negotium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {
    private List<SubItem> subItemList;
    RecyclerViewInterface recyclerViewInterface;
    Context context;

    public SubItemAdapter(List<SubItem> subItemList, RecyclerViewInterface recyclerViewInterface, Context context) {
        this.subItemList = subItemList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;

    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sub_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder holder, int position) {
        SubItem subItem = subItemList.get(position);
        Glide.with(context).load(Constants.ROOT_IMAGEURL+subItem.getpic()).into(holder.imageView);
        holder.tvSubItemTitle.setText("The product was requested by "+subItem.getSubItemTitle());
        holder.phoneno.setText("Contact - "+subItem.getphoneno());
        holder.quantity.setText("Quantity - "+subItem.getquantity());
        holder.productname.setText(subItem.getproduct_name());
        holder.totalprice.setText("The total cost with the quantity will be "+subItem.gettotalprice());
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubItemTitle,productname,totalprice,quantity,phoneno;
        ImageView imageView;
        Button acceptbtn,declinebtn;

        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_sub_item_title);
            productname = itemView.findViewById(R.id.productname);
            totalprice = itemView.findViewById(R.id.totalprice);
            quantity = itemView.findViewById(R.id.quantity);
            phoneno = itemView.findViewById(R.id.phoneno);
            imageView = itemView.findViewById(R.id.img_sub_item);
            acceptbtn = itemView.findViewById(R.id.acceptbtn);
            declinebtn = itemView.findViewById(R.id.declinebtn);
            acceptbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            int lol= Integer.parseInt(subItemList.get(pos).getbuyid());
                            recyclerViewInterface.onItemClick(lol);
                        }
                    }
                }
            });
            declinebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            int lol= Integer.parseInt(subItemList.get(pos).getbuyid());
                            recyclerViewInterface.onItemDelete(lol);
                        }
                    }
                }
            });
        }
    }
}
