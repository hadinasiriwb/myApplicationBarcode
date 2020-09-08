package com.example.myapplication;

public class ListViewItemDTO {
    private boolean checked = false;
    private String itemText = "";
    private String itemCount="";
    private String itemPrice="";
    private String itemBarcode="";
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public String getItemText() {
        return itemText;
    }
    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
    public String getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
    public String getItemCount() {
        return itemCount;
    }
    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
    public String getItemBarcode() {
        return itemBarcode;
    }
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
}