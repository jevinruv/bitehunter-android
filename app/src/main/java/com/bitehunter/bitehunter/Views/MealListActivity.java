package com.bitehunter.bitehunter.Views;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bitehunter.bitehunter.Adapters.RecycleAdapter_MenuListMeal;
import com.bitehunter.bitehunter.Adapters.RecycleAdapter_RestaurantList;
import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.RestaurantListTask;
import com.bitehunter.bitehunter.Helper.RestaurantMenuTask;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Model.Restaurant;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MealListActivity extends BaseActivity implements AsyncTaskCompleteListener<HashMap> {

    RecycleAdapter_MenuListMeal adapter;
    RecyclerView rv;
    View view;

    private ArrayList<Meal> listMeal = new ArrayList<>();
    int selectedRestaurant = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        view = getLayoutInflater().inflate(R.layout.activity_meal_list, contentFrameLayout);

        toolbarTitle.setText("Menu");

        selectedRestaurant = getIntent().getIntExtra(CommonTags.TAG_SELECTED_RESTAURANT, 0);
        new RestaurantMenuTask(view).execute(String.valueOf(selectedRestaurant));

        initViews();
    }

    public void initViews() {
        rv = (RecyclerView) view.findViewById(R.id.rv_meal);
    }


    public void loadMealList() {

        //SET ITS Properties
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        adapter = new RecycleAdapter_MenuListMeal(this, listMeal);
        rv.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        menu.clear();
        inflater.inflate(R.menu.search_view_menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = new SearchView(((MealListActivity) this).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchViewAndroidActionBar.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                adapter.getFilter().filter(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onTaskComplete(HashMap result) {

        for (Object entry : result.values()) {
            Meal value = (Meal) entry;
            listMeal.add(value);
        }
        loadMealList();
    }
}
