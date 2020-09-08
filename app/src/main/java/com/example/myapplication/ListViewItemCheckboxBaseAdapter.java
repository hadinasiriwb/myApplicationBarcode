package com.example.myapplication;
import android.app.LauncherActivity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
public class ListViewItemCheckboxBaseAdapter extends BaseAdapter {
    private List<ListViewItemDTO> listViewItemDtoList = null;
    private Context ctx = null;
    public ListViewItemCheckboxBaseAdapter(Context ctx, List<ListViewItemDTO> listViewItemDtoList) {
        this.ctx = ctx;
        this.listViewItemDtoList = listViewItemDtoList;
    }
    @Override
    public int getCount() {
        int ret = 0;
        if(listViewItemDtoList!=null)
        {
            ret = listViewItemDtoList.size();
        }
        return ret;
    }
    @Override
    public Object getItem(int itemIndex) {
        Object ret = null;
        if(listViewItemDtoList!=null) {
            ret = listViewItemDtoList.get(itemIndex);
        }
        return ret;
    }
    @Override
    public long getItemId(int itemIndex) {
        return itemIndex;
    }
    @Override
    public View getView(int itemIndex, View convertView, ViewGroup viewGroup) {
        ListViewItemViewHolder viewHolder = null;
        if(convertView!=null)
        {
            viewHolder = (ListViewItemViewHolder) convertView.getTag();
        }else
        {
            convertView = View.inflate(ctx, R.layout.activity_list_view_with_checkbox_item, null);
            CheckBox listItemCheckbox = (CheckBox) convertView.findViewById(R.id.list_view_item_checkbox);
            TextView listItemText = (TextView) convertView.findViewById(R.id.list_view_item_text);
            TextView listItemCount=(TextView) convertView.findViewById(R.id.list_view_count_text);
            TextView listItemPrice=(TextView) convertView.findViewById(R.id.list_view_price_text);
            TextView listItemBarcode=(TextView)convertView.findViewById(R.id.list_view_barcode_text);
            viewHolder = new ListViewItemViewHolder(convertView);
            viewHolder.setItemCheckbox(listItemCheckbox);
            viewHolder.setItemTextView(listItemText);
            viewHolder.setItemCountTextView(listItemCount);
            viewHolder.setItemPriceTextView(listItemPrice);
            viewHolder.setItemBarcodeTextView(listItemBarcode);
            convertView.setTag(viewHolder);
        }
        ListViewItemDTO listViewItemDto = listViewItemDtoList.get(itemIndex);
        viewHolder.getItemCheckbox().setChecked(listViewItemDto.isChecked());
        viewHolder.getItemTextView().setText(listViewItemDto.getItemText());
        viewHolder.getItempriceTextView().setText(listViewItemDto.getItemPrice());
        viewHolder.getItemcountTextView().setText(listViewItemDto.getItemCount());
        viewHolder.getItemBarcodeTextView().setText(listViewItemDto.getItemBarcode());
        return convertView;
    }
}