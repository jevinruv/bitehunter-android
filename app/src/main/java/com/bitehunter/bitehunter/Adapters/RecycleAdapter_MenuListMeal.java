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
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.CustomFilterMeal;
import com.bitehunter.bitehunter.Others.CustomVolleyRequest;
import com.bitehunter.bitehunter.Others.LocalDatabaseHandler;
import com.bitehunter.bitehunter.R;
import com.bitehunter.bitehunter.Views.MealDetailsActivity;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Jevin on 26-Dec-17.
 */

public class RecycleAdapter_MenuListMeal extends RecyclerView.Adapter<RecycleAdapter_MenuListMeal.MyViewHolder> implements Filterable {

    Context context;
    View itemView;
    private ImageLoader imageLoader;
    ImageButton btn_add_to_order;

    public List<Meal> mealList, filterList;
    CustomFilterMeal filterMeal;

    public HashMap<String, String> selectedMenuMealIdAndName = new HashMap<>();
    public HashMap<String, Double> selectedMenuMealIdAndPrice = new HashMap<>();

    LocalDatabaseHandler db;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView meal_image;
        TextView meal_label_name;
        TextView meal_label_price;
        ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
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


    public RecycleAdapter_MenuListMeal(Context context, List<Meal> mealList) {
        this.mealList = mealList;
        this.filterList = mealList;
        this.context = context;

        db = new LocalDatabaseHandler(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_meal_list, parent, false);

        return new MyViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Meal meal = mealList.get(position);

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
                Intent mealDetails = new Intent(context, MealDetailsActivity.class);

                String itemID = mealList.get(pos).getItemID();

                mealDetails.putExtra(CommonTags.TAG_MEAL_ITEM_ID, itemID);
                context.startActivity(mealDetails);
            }
        });

        btn_add_to_order = (ImageButton) itemView.findViewById(R.id.btn_add_to_order);
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
        });

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void setItems(List<Meal> mealList) {
        this.mealList = mealList;
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filterMeal == null) {
            filterMeal = new CustomFilterMeal(filterList, this);
        }

        return filterMeal;
    }

}

