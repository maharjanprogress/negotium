package com.example.negotium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemUserAdapter extends RecyclerView.Adapter<ItemUserAdapter.ItemUserViewHolder>{
    private List<ItemUser> itemList;
    private RecyclerViewInterface recyclerViewInterface;
    private Context context;

    public ItemUserAdapter(List<ItemUser> itemList, RecyclerViewInterface recyclerViewInterface, Context context) {
        this.itemList = itemList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemUserAdapter.ItemUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemUserAdapter.ItemUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemUserAdapter.ItemUserViewHolder holder, int position) {
        ItemUser item = itemList.get(position);
        holder.tvItemTitle.setText(item.getItemTitle());
        holder.callno.setText("Phone no: "+item.getcallno());
        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvSubItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

        // Create sub item view adapter
        SubItemUserAdapter subItemAdapter = new SubItemUserAdapter(item.getSubItemList(),recyclerViewInterface,context);

        holder.rvSubItem.setLayoutManager(layoutManager);
        holder.rvSubItem.setAdapter(subItemAdapter);
//        holder.rvSubItem.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemUserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle,callno;
        private RecyclerView rvSubItem;
        Button alldeliver,alldecline;

        ItemUserViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            callno = itemView.findViewById(R.id.callno);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
            alldeliver = itemView.findViewById(R.id.alldeliverbtn);
            alldecline = itemView.findViewById(R.id.declineallbtn);
            alldeliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onDateAccept(itemList.get(pos).getid());
                        }
                    }
                }
            });
            alldecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onDateDecline(itemList.get(pos).getid());
                        }
                    }
                }
            });
        }
    }
}
