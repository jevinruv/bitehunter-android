package com.bitehunter.bitehunter.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitehunter.bitehunter.Adapters.RecycleAdapter_MenuListMeal;
import com.bitehunter.bitehunter.Adapters.RecycleAdapter_MenuListMealSelect;
import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.RestaurantMenuTask;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Others.LocalDatabaseHandler;
import com.bitehunter.bitehunter.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectedMealsFragment extends Fragment {

    private ArrayList<Meal> mealList;
    RecyclerView rv;
    RecycleAdapter_MenuListMealSelect adapter;
    private View view;


    public static SelectedMealsFragment newInstance() {
        SelectedMealsFragment fragment = new SelectedMealsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_selected_meals, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocalDatabaseHandler db = new LocalDatabaseHandler(getContext());
        mealList = db.getAllMeals();
        loadMenu();
    }

    public void loadMenu() {
        rv = (RecyclerView) view.findViewById(R.id.recyclerview_restaurant_menu_selected);

        //SET ITS PROPETRIES
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        adapter = new RecycleAdapter_MenuListMealSelect(this.getActivity(), mealList);
        rv.setAdapter(adapter);
    }

}
