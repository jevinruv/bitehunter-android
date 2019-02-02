package com.bitehunter.bitehunter.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.bitehunter.bitehunter.Adapters.RecycleAdapter_Meal;
import com.bitehunter.bitehunter.Adapters.RecycleAdapter_Restaurant;
import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.HomePopulateTask;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Model.Restaurant;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.M)
public class HomeActivity extends BaseActivity implements AsyncTaskCompleteListener<HashMap> {

    private RecyclerView recyclerView_meal;
    private RecyclerView recyclerView_restaurant;
    private RecycleAdapter_Meal adapter_meal;
    private RecycleAdapter_Restaurant adapter_restaurant;
    private View view;

    HashMap<String, Object> homeActivityItems = new HashMap<>();
    List<Meal> mealList = new ArrayList<>();
    List<Restaurant> restaurantList = new ArrayList<>();

    SharedPreferences sharedpreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        view = getLayoutInflater().inflate(R.layout.activity_home, contentFrameLayout);

        toolbarTitle.setText("Home");

        //getSharedPrefs();

        new HomePopulateTask(view).execute();

        //meal recyclerview

        recyclerView_meal = (RecyclerView) findViewById(R.id.recyclerview_meal);
        RecyclerView.LayoutManager mealLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_meal.setLayoutManager(mealLayoutManager);
        recyclerView_meal.setItemAnimator(new DefaultItemAnimator());

        adapter_meal = new RecycleAdapter_Meal(HomeActivity.this, mealList);
        recyclerView_meal.setAdapter(adapter_meal);


        //restaurant recyclerview

        recyclerView_restaurant = (RecyclerView) findViewById(R.id.recyclerview_restaurant);
        RecyclerView.LayoutManager restaurantLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_restaurant.setLayoutManager(restaurantLayoutManager);
        recyclerView_restaurant.setItemAnimator(new DefaultItemAnimator());

        adapter_restaurant = new RecycleAdapter_Restaurant(HomeActivity.this, restaurantList);
        recyclerView_restaurant.setAdapter(adapter_restaurant);

    }


    @Override
    public void onTaskComplete(HashMap result) {

        homeActivityItems = result;

        mealList = (List<Meal>) homeActivityItems.get(CommonTags.TAG_MEAL_LIST);
        restaurantList = (List<Restaurant>) homeActivityItems.get(CommonTags.TAG_RESTAURANT_LIST);

        updateAdapters();
    }

    public void updateAdapters() {
        adapter_meal.setItems(mealList);
        adapter_meal.notifyDataSetChanged();

        adapter_restaurant.setItems(restaurantList);
        adapter_restaurant.notifyDataSetChanged();
    }

/*    public void getSharedPrefs() {

        sharedpreferences = getSharedPreferences(CommonTags.MyPREFERENCES, Context.MODE_PRIVATE);

        String customerId = sharedpreferences.getString(CommonTags.CUSTOMER_ID, null);
        String customerName = sharedpreferences.getString(CommonTags.CUSTOMER_NAME, null);
        String customerEmail = sharedpreferences.getString(CommonTags.CUSTOMER_EMAIL, null);

        CommonMethods.displayToast(this, customerId + " " + customerName + " " + customerEmail + " " );

    }*/
}

