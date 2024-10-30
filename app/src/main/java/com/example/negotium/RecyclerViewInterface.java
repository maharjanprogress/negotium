package com.example.negotium;

public interface RecyclerViewInterface {
    void onItemClick(int position);
    void onItemUpdate(int position);
    void onItemDelete(int position);
    void onDateAccept(String date);
    void onDateDecline(String date);
}
