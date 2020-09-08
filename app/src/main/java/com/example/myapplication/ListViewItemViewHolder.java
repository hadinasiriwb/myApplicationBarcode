package com.example.myapplication;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
public class ListViewItemViewHolder extends RecyclerView.ViewHolder {
    private CheckBox itemCheckbox;
    private TextView itemTextView;
    private TextView itemcountTextView;
    private TextView itempriceTextView;
    private TextView itembarcodeTextView;
    public ListViewItemViewHolder(View itemView) {
        super(itemView);
    }
    public CheckBox getItemCheckbox() {
        return itemCheckbox;
    }
    public void setItemCheckbox(CheckBox itemCheckbox) {
        this.itemCheckbox = itemCheckbox;
    }
    public TextView getItemTextView() {
        return itemTextView;
    }
    public void setItemTextView(TextView itemTextView) {
        this.itemTextView = itemTextView;
    }
    public void setItemCountTextView(TextView itemTextView) {
        this.itemcountTextView = itemTextView;
    }
    public TextView getItempriceTextView() {
        return itempriceTextView;
    }
    public void setItemPriceTextView(TextView itemTextView) {
        this.itempriceTextView = itemTextView;
    }
    public TextView getItemcountTextView() {
        return itemcountTextView;
    }
    public void setItemBarcodeTextView(TextView itemTextView) {
        this.itembarcodeTextView = itemTextView;
    }
    public TextView getItemBarcodeTextView() {
        return itembarcodeTextView;
    }
}