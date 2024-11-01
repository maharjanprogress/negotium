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

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>{
    private List<ChatClass> chatClasses;
    private RecyclerViewInterface recyclerViewInterface;
    private Context context;

    public ChatListAdapter(List<ChatClass> chatClasses, RecyclerViewInterface recyclerViewInterface, Context context) {
        this.chatClasses = chatClasses;
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlisthead,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.bindChatList(chatClasses.get(position));
    }

    @Override
    public int getItemCount() {
        return chatClasses.size();
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder{
        private TextView username,phoneno,youorme,lastmessage;
        private View viewBackground;
        private ImageView imageSelected;
        private ConstraintLayout chatlayout;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            chatlayout = itemView.findViewById(R.id.chatlistlayout);
            viewBackground = itemView.findViewById(R.id.viewBackgroundd);
            imageSelected = itemView.findViewById(R.id.imageSelectedd);
            phoneno = itemView.findViewById(R.id.PhoneNo);
            username = itemView.findViewById(R.id.username);
            youorme = itemView.findViewById(R.id.youorme);
            lastmessage = itemView.findViewById(R.id.lastmessage);
        }
        void bindChatList(final ChatClass chatClass){
            username.setText(chatClass.username);
            lastmessage.setText(chatClass.text);
            phoneno.setText("Or Call in "+chatClass.phoneno);
            if (chatClass.isseen){
                viewBackground.setBackgroundResource(R.drawable.nonewbackground);
                imageSelected.setVisibility(View.GONE);
            }
            else {
                viewBackground.setBackgroundResource(R.drawable.newmessage);
                imageSelected.setVisibility(View.VISIBLE);
            }
            if (chatClass.sentbyuser){
                youorme.setText(chatClass.username+":");
            }
            else {
                youorme.setText("ME:");
            }
            chatlayout.setOnClickListener(new View.OnClickListener() {
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
