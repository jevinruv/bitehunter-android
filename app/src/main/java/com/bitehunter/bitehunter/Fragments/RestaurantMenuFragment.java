package com.bitehunter.bitehunter.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bitehunter.bitehunter.Adapters.RecycleAdapter_Meal;
import com.bitehunter.bitehunter.Adapters.RecycleAdapter_MenuListMeal;
import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.RestaurantMenuTask;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;
import com.bitehunter.bitehunter.Views.RestaurantMenuActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class RestaurantMenuFragment extends Fragment {

    SearchView searchViewAndroidActionBar;
    RecycleAdapter_MenuListMeal adapter;
    RecyclerView rv;
    View view;

    private int selectedRestaurant = 0;
    private HashMap<String, Meal> restaurantMenu = new HashMap<>();
    private ArrayList<Meal> mealList;


    public static RestaurantMenuFragment newInstance() {
        RestaurantMenuFragment fragment = new RestaurantMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
        selectedRestaurant = getArguments().getInt(CommonTags.TAG_SELECTED_RESTAURANT);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RestaurantMenuTask restaurantMenuTask = new RestaurantMenuTask(getActivity(), new AsyncTaskCompleteListener<HashMap>() {

            @Override
            public void onTaskComplete(HashMap result) {
                restaurantMenu = result;
                mealList = new ArrayList<Meal>(restaurantMenu.values());
                loadMenu();
            }
        });

        restaurantMenuTask.execute(String.valueOf(selectedRestaurant));
    }

    public void loadMenu() {
        rv = (RecyclerView) view.findViewById(R.id.recyclerview_restaurant_menu);

        //SET ITS Properties
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        adapter = new RecycleAdapter_MenuListMeal(this.getActivity(), mealList);
        rv.setAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.search_view_menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = new SearchView(((RestaurantMenuActivity) this.getActivity()).getSupportActionBar().getThemedContext());
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
    }


}
