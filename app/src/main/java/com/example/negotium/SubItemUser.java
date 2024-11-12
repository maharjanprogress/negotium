package com.example.negotium;

public class SubItemUser {
    private String subItemTitle,buyid,quantity,phoneno,product_name,pic,totalprice;

    public SubItemUser(String subItemTitle, String buyid, String quantity, String phoneno, String product_name, String pic, String totalprice) {
        this.subItemTitle = subItemTitle;
        this.buyid = buyid;
        this.quantity = quantity;
        this.phoneno = phoneno;
        this.product_name = product_name;
        this.pic = pic;
        this.totalprice = totalprice;
    }

    public SubItemUser(String subItemTitle) {
        this.subItemTitle = subItemTitle;
    }

    public String getSubItemTitle() {
        return subItemTitle;
    }
    public String getbuyid() {
        return buyid;
    }
    public String getquantity() {
        return quantity;
    }
    public String getphoneno() {
        return phoneno;
    }
    public String getproduct_name() {
        return product_name;
    }
    public String gettotalprice() {
        return totalprice;
    }
    public String getpic() {
        return pic;
    }

    public void setSubItemTitle(String subItemTitle) {
        this.subItemTitle = subItemTitle;
    }
}
