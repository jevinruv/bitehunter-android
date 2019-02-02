package com.bitehunter.bitehunter.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.CustomFilterMeal;
import com.bitehunter.bitehunter.Others.CustomVolleyRequest;
import com.bitehunter.bitehunter.R;
import com.bitehunter.bitehunter.Views.MealDetailsActivity;


import java.util.List;

/**
 * Created by Jevin on 26-Dec-17.
 */

public class RecycleAdapter_Meal extends RecyclerView.Adapter<RecycleAdapter_Meal.MyViewHolder> {

    Context context;
    public List<Meal> mealList;
    private ImageLoader imageLoader;
    CustomFilterMeal filterMeal;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //public NetworkImageView meal_image;
        public ImageView meal_image;
        TextView meal_label_name;
        TextView meal_label_price;

        ItemClickListener itemClickListener;


        public MyViewHolder(View view) {
            super(view);
            //meal_image = (NetworkImageView) view.findViewById(R.id.meal_image);
            meal_image = (ImageView) view.findViewById(R.id.meal_image);
            meal_label_name = (TextView) view.findViewById(R.id.meal_label_name);
            meal_label_price = (TextView) view.findViewById(R.id.meal_label_price);

            view.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }


    public RecycleAdapter_Meal(Context context, List<Meal> mealList) {
        this.mealList = mealList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_meal_main, parent, false);

        return new MyViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Meal meal = mealList.get(position);

        holder.meal_label_name.setText(meal.getName());
        holder.meal_label_price.setText(meal.getPrice());

        //Loading restaurant_image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();

/*
        imageLoader.get(meal.getImage(), ImageLoader.getImageListener(holder.meal_image, R.drawable.img_white, android.R.drawable.ic_dialog_alert));
        holder.meal_image.setImageUrl(meal.getImage(), imageLoader);
*/


        imageLoader.get(meal.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                Bitmap mealImage = response.getBitmap();
                if (mealImage != null) {
                    mealImage = CommonMethods.getRoundedCornerBitmap(mealImage);
                    holder.meal_image.setImageBitmap(mealImage);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                //CommonMethods.displayToast(context, "heere " + pos);
                Intent mealDetails = new Intent(context, MealDetailsActivity.class);

                String itemID = mealList.get(pos).getItemID();

                mealDetails.putExtra(CommonTags.TAG_MEAL_ITEM_ID, itemID);
                context.startActivity(mealDetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void setItems(List<Meal> mealList) {
        this.mealList = mealList;
    }

}

