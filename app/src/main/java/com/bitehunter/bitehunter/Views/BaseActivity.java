package com.bitehunter.bitehunter.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;


public class BaseActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    CommonMethods commonMethods;
    TextView toolbarTitle;
    android.support.v7.app.ActionBar actionBar;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        sharedpreferences = getSharedPreferences(CommonTags.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View hView = navigationView.getHeaderView(0);
        TextView nav_user_name = (TextView) hView.findViewById(R.id.name);
        TextView nav_user_email = (TextView) hView.findViewById(R.id.email);

        String name = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_NAME, null);
        String email = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_EMAIL, null);

        nav_user_name.setText(name);
        nav_user_email.setText(email);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        //Customize the ActionBar
        actionBar = getSupportActionBar();
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar

        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        toolbarTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.color.transparent_action);
        actionBar.setHomeButtonEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String appPackageName = getPackageName();

                switch (item.getItemId()) {

                    case R.id.nav_home:
                        Intent main = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(main);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_make_reservation:
                        Intent makeReservation = new Intent(getApplicationContext(), MakeReservation.class);
                        startActivity(makeReservation);
                        //finish();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_manage_account:
                        Intent manageAccount = new Intent(getApplicationContext(), UpdateCustomerActivity.class);
                        startActivity(manageAccount);
                        //finish();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_restaurants:
                        Intent restaurants = new Intent(getApplicationContext(), RestaurantListActivity.class);
                        startActivity(restaurants);
                        //finish();
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.nav_logout:
                        clearUserData();
                        Intent logoutintent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(logoutintent);
                        finish();
                        break;
                }
                return false;
            }
        });

    }

    public void clearUserData() {
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void logOut() {
        try {
            getApplicationContext().getSharedPreferences("userInfo", 0).edit().clear().commit();

/*            Intent intent  = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
        } catch (Exception e) {

        }
    }

    public SharedPreferences getSharedPrefs() {

        sharedpreferences = getSharedPreferences(CommonTags.MyPREFERENCES, Context.MODE_PRIVATE);

        String customerId = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_ID, null);
        String customerName = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_NAME, null);
        String customerEmail = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_EMAIL, null);

        return sharedpreferences;
    }
}
