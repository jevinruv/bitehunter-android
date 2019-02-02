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
import com.bitehunter.bitehunter.Model.Restaurant;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.CustomVolleyRequest;
import com.bitehunter.bitehunter.R;
import com.bitehunter.bitehunter.Views.HomeActivity;
import com.bitehunter.bitehunter.Views.MealDetailsActivity;
import com.bitehunter.bitehunter.Views.RestaurantDetailsActivity;


import java.util.List;


/**
 * Created by Rp on 6/14/2016.
 */
public class RecycleAdapter_Restaurant extends RecyclerView.Adapter<RecycleAdapter_Restaurant.MyViewHolder> {
    Context context;
    private List<Restaurant> restaurantList;
    //Imageloader to load restaurant_image
    private ImageLoader imageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView restaurant_image;
        TextView restaurant_name;
        TextView city;

        ItemClickListener itemClickListener;


        public MyViewHolder(View view) {
            super(view);
            restaurant_image = (ImageView) view.findViewById(R.id.meal_image);
            restaurant_name = (TextView) view.findViewById(R.id.meal_label_name);

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


    public RecycleAdapter_Restaurant(HomeActivity context, List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_restaurant_main, parent, false);

        return new MyViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        holder.restaurant_name.setText(restaurant.getName());
        //holder.city.setText(restaurant.getCity());

        //Loading restaurant_image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();

/*        imageLoader.get(restaurant.getImage(), ImageLoader.getImageListener(holder.restaurant_image, R.drawable.img_white, android.R.drawable.ic_dialog_alert));
        holder.restaurant_image.setImageUrl(restaurant.getImage(), imageLoader);

        */

        imageLoader.get(restaurant.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                Bitmap restaurantImage = response.getBitmap();
                if(restaurantImage != null){
                    restaurantImage = CommonMethods.getRoundedCornerBitmap(restaurantImage);
                    holder.restaurant_image.setImageBitmap(restaurantImage);
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
                Intent restaurantDetails = new Intent(context, RestaurantDetailsActivity.class);

                int restaurantId = restaurantList.get(pos).getId();

                restaurantDetails.putExtra(CommonTags.TAG_RESTAURANT_ID, restaurantId);
                context.startActivity(restaurantDetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


    public void setItems(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }


}


