package com.bitehunter.bitehunter.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitehunter.bitehunter.Model.Item;
import com.bitehunter.bitehunter.R;

import java.util.List;

/**
 * Created by Ludowica on 19/12/2017.
 */

public class MenuItemAdapter extends ArrayAdapter<Item>{

    public static List<Item> menuList;
    Context context;

    public MenuItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> menuList) {
        super(context, resource, menuList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater menuListInflater = LayoutInflater.from(getContext());
        View customMenuView = menuListInflater.inflate(R.layout.adapter_meal_list, parent, false);

        final String name = menuList.get(position).getItemName();
        final double price = menuList.get(position).getItemPrice();
        final String desc = menuList.get(position).getItemDesc();

        TextView itemName = (TextView) customMenuView.findViewById(R.id.meal_label_name);
        TextView itemPrice = (TextView) customMenuView.findViewById(R.id.meal_label_price);
        ImageView itemImage = (ImageView) customMenuView.findViewById(R.id.meal_image);
        //Button btnAdd = (Button) customMenuView.findViewById(R.id.btnAddToOrder);

        /*int resId = context.getResources().getIdentifier(itemImage, "drawable", context.getPackageName());
        itemImage.setImageResource(resId);*/

        return super.getView(position, convertView, parent);
    }


























}
