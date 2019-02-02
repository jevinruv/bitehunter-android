package com.bitehunter.bitehunter.Others;

import android.widget.Filter;

import com.bitehunter.bitehunter.Adapters.RecycleAdapter_MenuListMeal;
import com.bitehunter.bitehunter.Adapters.RecycleAdapter_RestaurantList;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevin on 31-Dec-17.
 */

public class CustomFilterRestaurant extends Filter {

    RecycleAdapter_RestaurantList adapter;
    List<Restaurant> filterList;


    public CustomFilterRestaurant(List<Restaurant> filterList, RecycleAdapter_RestaurantList adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    //FILTERING OCCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            List<Object> filteredMeals = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                //CHECK
                if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredMeals.add(filterList.get(i));
                }
            }

            results.count = filteredMeals.size();
            results.values = filteredMeals;
        } else {
            results.count = filterList.size();
            results.values = filterList;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.restaurantList = (List<Restaurant>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
