package com.bitehunter.bitehunter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bitehunter.bitehunter.Model.Rate;
import com.bitehunter.bitehunter.R;

import java.util.ArrayList;

/**
 * Created by Pasan .M. Semage on 2018-01-22.
 */

public class RateAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Rate> list;

    public RateAdapter(Context context, int layout, ArrayList<Rate> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {

        TextView txtRUserName, txtRDate, txtRReview;
        RatingBar ratingRrate;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.ratingRrate = (RatingBar) row.findViewById(R.id.ratingRrate);
            holder.txtRDate = (TextView) row.findViewById(R.id.txtRDate);
            holder.txtRReview = (TextView) row.findViewById(R.id.txtRReview);
            holder.txtRUserName = (TextView) row.findViewById(R.id.txtRUserName);


            row.setTag(holder);
        } else {

            holder = (ViewHolder) row.getTag();

        }

        Rate rate = list.get(position);

        holder.txtRUserName.setText(rate.getCustomerName());
        holder.ratingRrate.setRating(rate.getRate());
        holder.txtRDate.setText(rate.getDate());
        holder.txtRReview.setText(rate.getReview());

        return row;
    }
}
