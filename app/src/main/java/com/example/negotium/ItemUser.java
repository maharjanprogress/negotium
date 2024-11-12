package com.example.negotium;

import java.util.List;

public class ItemUser {
    private String itemTitle,callno,id;
    private List<SubItemUser> subItemList;

    public ItemUser(String itemTitle,String callno,String id, List<SubItemUser> subItemList) {
        this.itemTitle = itemTitle;
        this.callno = callno;
        this.id = id;
        this.subItemList = subItemList;
    }

    public String getItemTitle() {
        return itemTitle;
    }
    public String getid() {
        return id;
    }
    public String getcallno() {
        return callno;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<SubItemUser> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(List<SubItemUser> subItemList) {
        this.subItemList = subItemList;
    }
}
