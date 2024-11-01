package com.example.negotium;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private RecyclerViewInterface recyclerViewInterface;

    private List<MessageList> messageList;
    public static Boolean isadmin;

    public MessageAdapter(List<MessageList> messageList,RecyclerViewInterface recyclerViewInterface,Boolean isadmin) {
        this.messageList = messageList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.isadmin = isadmin;
    }

    @Override
    public int getItemViewType(int position) {
        if (isadmin){
            if (messageList.get(position).sentbyuser) {
                return VIEW_TYPE_RECEIVED;
            } else {
                return VIEW_TYPE_SENT;
            }
        }else {
            if (messageList.get(position).sentbyuser) {
                return VIEW_TYPE_SENT;
            } else {
                return VIEW_TYPE_RECEIVED;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_recieved, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageList message = messageList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(message);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder for Sent Messages
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, dateText,seentext;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textplacesent);
            dateText = itemView.findViewById(R.id.dateandtimesent);
            seentext = itemView.findViewById(R.id.seensent);
        }

        void bind(MessageList message) {
            messageText.setText(message.message);
            dateText.setText(message.date);
            if (isadmin) {
                if (message.isseenuser) {
                    seentext.setText("seen");
                } else {
                    seentext.setText("unseen");

                }
            }else {
                if (message.isseenadmin) {
                    seentext.setText("seen");
                } else {
                    seentext.setText("unseen");

                }
            }
            messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(message.isselected){
                        dateText.setVisibility(View.GONE);
                        seentext.setVisibility(View.GONE);
                        message.isselected=false;
                    }else {
                        dateText.setVisibility(View.VISIBLE);
                        seentext.setVisibility(View.VISIBLE);
                        message.isselected=true;
                    }
                }
            });
        }
    }

    // ViewHolder for Received Messages
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, dateText,seentext;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textplacerecieved);
            dateText = itemView.findViewById(R.id.dateandtimerecieved);
            seentext = itemView.findViewById(R.id.seenrecieved);
        }

        void bind(MessageList message) {
            messageText.setText(message.message);
            dateText.setText(message.date);
            if (isadmin) {
                if (message.isseenuser) {
                    seentext.setText("seen");
                } else {
                    seentext.setText("unseen");

                }
            }else {
                if (message.isseenadmin) {
                    seentext.setText("seen");
                } else {
                    seentext.setText("unseen");

                }
            }
            messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(message.isselected){
                        dateText.setVisibility(View.GONE);
                        seentext.setVisibility(View.GONE);
                        message.isselected=false;
                    }else {
                        dateText.setVisibility(View.VISIBLE);
                        seentext.setVisibility(View.VISIBLE);
                        message.isselected=true;
                    }
                }
            });
        }
    }
}
