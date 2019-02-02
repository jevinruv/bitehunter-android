package com.bitehunter.bitehunter.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.bitehunter.bitehunter.Adapters.RecycleAdapter_MenuListMeal;
import com.bitehunter.bitehunter.Fragments.RestaurantMenuFragment;
import com.bitehunter.bitehunter.Fragments.SelectedMealsFragment;
import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.RestaurantMenuTask;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.LocalDatabaseHandler;
import com.bitehunter.bitehunter.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMenuActivity extends AppCompatActivity {

    private View view;
    //private RelativeLayout restaurantMenu;
    private int selectedRestaurant = 0;

    private HashMap<String, Meal> restaurantMenu = new HashMap<>();
    private ArrayList<Meal> mealList;

    HashMap<String, Object> selectedMenu = new HashMap<>();
    LocalDatabaseHandler db = new LocalDatabaseHandler(this);

    HashMap<String, String> selectedMenuMealIdAndName = new HashMap<>();
    HashMap<String, Double> selectedMenuMealIdAndPrice = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        selectedRestaurant = getIntent().getIntExtra(CommonTags.TAG_SELECTED_RESTAURANT, 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    public void initViews() {

        view = (RelativeLayout) findViewById(R.id.activity_restaurant_menu);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {

                            case R.id.action_item1:

                                selectedFragment = RestaurantMenuFragment.newInstance();
                                Bundle bundle = new Bundle();
                                bundle.putInt(CommonTags.TAG_SELECTED_RESTAURANT, selectedRestaurant);
                                selectedFragment.setArguments(bundle);
                                break;

                            case R.id.action_item2:
                                selectedFragment = SelectedMealsFragment.newInstance();
                                break;
                        }

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment restaurantMenuFragment = RestaurantMenuFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt(CommonTags.TAG_SELECTED_RESTAURANT, selectedRestaurant);
        restaurantMenuFragment.setArguments(bundle);

        transaction.replace(R.id.frame_layout, restaurantMenuFragment);
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    public void onBackPressed() {

        sendSelectedMeals();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            sendSelectedMeals();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendSelectedMeals() {
        Intent intent = new Intent();
        this.selectedMenuMealIdAndName = db.getAllMealIdAndName();
        this.selectedMenuMealIdAndPrice = db.getAllMealIdAndPrice();

        selectedMenu.put(CommonTags.TAG_SELECTED_MENU_MEAL_ID_AND_NAME, selectedMenuMealIdAndName);
        selectedMenu.put(CommonTags.TAG_SELECTED_MENU_MEAL_ID_AND_PRICE, selectedMenuMealIdAndPrice);

        intent.putExtra(CommonTags.TAG_SELECTED_MENU, selectedMenu);
        setResult(RESULT_OK, intent);
    }

}
