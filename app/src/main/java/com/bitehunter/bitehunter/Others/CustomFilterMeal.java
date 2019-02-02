package com.bitehunter.bitehunter.Others;

import android.widget.Adapter;
import android.widget.Filter;

import com.bitehunter.bitehunter.Adapters.RecycleAdapter_MenuListMeal;
import com.bitehunter.bitehunter.Model.Meal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevin on 31-Dec-17.
 */

public class CustomFilterMeal extends Filter {

    RecycleAdapter_MenuListMeal adapter;
    List<Meal> filterList;


    public CustomFilterMeal(List<Meal> filterList, RecycleAdapter_MenuListMeal adapter) {
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

        adapter.mealList = (List<Meal>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
