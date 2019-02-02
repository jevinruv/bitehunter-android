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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Model.Restaurant;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.CustomFilterMeal;
import com.bitehunter.bitehunter.Others.CustomFilterRestaurant;
import com.bitehunter.bitehunter.Others.CustomVolleyRequest;
import com.bitehunter.bitehunter.Others.LocalDatabaseHandler;
import com.bitehunter.bitehunter.R;
import com.bitehunter.bitehunter.Views.MealDetailsActivity;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Jevin on 26-Dec-17.
 */

public class RecycleAdapter_RestaurantList extends RecyclerView.Adapter<RecycleAdapter_RestaurantList.MyViewHolder> implements Filterable {

    Context context;
    View itemView;
    private ImageLoader imageLoader;
    ImageButton btn_add_to_order;

    public List<Restaurant> restaurantList, filterList;
    CustomFilterRestaurant filterRestaurant;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView restaurant_image;
        TextView restaurant_label_name;
        ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            restaurant_image = (ImageView) view.findViewById(R.id.restaurant_image);
            restaurant_label_name = (TextView) view.findViewById(R.id.restaurant_label_name);

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


    public RecycleAdapter_RestaurantList(Context context, List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        this.filterList = restaurantList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_restaurant_list, parent, false);

        return new MyViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Restaurant restaurant = restaurantList.get(position);

        holder.restaurant_label_name.setText(restaurant.getName());
        //holder.meal_label_price.setText(meal.getPrice());

        //Loading restaurant_image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();


        imageLoader.get(restaurant.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                Bitmap mealImage = response.getBitmap();
                if (mealImage != null) {
                    mealImage = CommonMethods.getRoundedCornerBitmap(mealImage);
                    holder.restaurant_image.setImageBitmap(mealImage);
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

                Intent restaurantDetails = new Intent(context, MealDetailsActivity.class);
                int restaurantId = restaurantList.get(pos).getId();
                restaurantDetails.putExtra(CommonTags.TAG_MEAL_ITEM_ID, restaurantId);
                context.startActivity(restaurantDetails);
            }
        });

/*        btn_add_to_order = (ImageButton) itemView.findViewById(R.id.btn_add_to_order);
        btn_add_to_order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectedMenuMealIdAndName.put(meal.getItemID(), meal.getName());
                selectedMenuMealIdAndPrice.put(meal.getItemID(), Double.parseDouble(meal.getPrice()));

                int p = holder.getAdapterPosition();
                Meal m = mealList.get(p);

                db.addMeal(m);
                CommonMethods.displaySnackbar(v, "Meal Added");
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public void setItems(List<Meal> mealList) {
        this.restaurantList = restaurantList;
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filterRestaurant == null) {
            filterRestaurant = new CustomFilterRestaurant(filterList, this);
        }

        return filterRestaurant;
    }

}

