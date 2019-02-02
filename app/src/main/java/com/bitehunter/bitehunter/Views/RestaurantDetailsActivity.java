package com.bitehunter.bitehunter.Views;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.RestaurantsDetailsTask;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.CustomVolleyRequest;
import com.bitehunter.bitehunter.R;

import java.util.HashMap;

public class RestaurantDetailsActivity extends AppCompatActivity implements AsyncTaskCompleteListener<HashMap> {

    ImageView restaurantImageExpanded;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NestedScrollView contentRestaurantDetails;
    private ImageLoader imageLoader;
    TextView openTime;
    TextView closeTime;
    TextView description;
    Button btnAddRate, btnViewRestaurantRate;

    HashMap<String, String> restaurantDetails = new HashMap<>();
    int restaurantId;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

		//getting the details of the view from the layout file and assigning to the variable
        view = getWindow().getDecorView().findViewById(android.R.id.content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restaurantId = getIntent().getExtras().getInt(CommonTags.TAG_RESTAURANT_ID);

        initViews();

		//executing a task
        new RestaurantsDetailsTask(view).execute(String.valueOf(restaurantId));

        rateAndReview();
    }

    private void rateAndReview() {
        btnAddRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateAndReview review = new RateAndReview(RestaurantDetailsActivity.this);
                review.rateRestaurent(String.valueOf(restaurantId).trim());
            }
        });

        btnViewRestaurantRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailsActivity.this, RateAndReviewActivity.class);
                intent.putExtra("RestID", restaurantId);
                startActivity(intent);
            }
        });
    }

    private void initViews() {

	//retrieiving details from the layout file and assigning them to variables
        restaurantImageExpanded = (ImageView) findViewById(R.id.restaurant_image_expanded);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        contentRestaurantDetails = (NestedScrollView) findViewById(R.id.content_restaurant_details);
        openTime = (TextView) contentRestaurantDetails.findViewById(R.id.label_open_time);
        closeTime = (TextView) contentRestaurantDetails.findViewById(R.id.label_close_time);
        description = (TextView) contentRestaurantDetails.findViewById(R.id.desc);
        btnAddRate = (Button)contentRestaurantDetails.findViewById(R.id.btnAddRate);
        btnViewRestaurantRate = (Button)contentRestaurantDetails.findViewById(R.id.btnViewResturantRate);
    }


    @Override
    public void onTaskComplete(HashMap result) {
        restaurantDetails = result;
        setValues();
    }

    private void setValues() {
		//retrieving restaurant details from the HashMap and assigning them to String variables
        String restaurantName = restaurantDetails.get(CommonTags.TAG_RESTAURANT_NAME);
        String restaurantImage = restaurantDetails.get(CommonTags.TAG_RESTAURANT_IMAGE);
        String restaurantOpenTime = restaurantDetails.get(CommonTags.TAG_RESTAURANT_TIME_OPEN);
        String restaurantCloseTime = restaurantDetails.get(CommonTags.TAG_RESTAURANT_TIME_CLOSE);
        String restaurantDescription = restaurantDetails.get(CommonTags.TAG_RESTAURANT_DESCRIPTION);

        collapsingToolbarLayout.setTitle(restaurantName);

        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();

		//loading the image in the ImageView
        imageLoader.get(restaurantImage, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                restaurantImageExpanded.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

		//setting the opening time, closing time and the description of the restaurant as labels
        openTime.setText(restaurantOpenTime);
        closeTime.setText(restaurantCloseTime);
        description.setText(restaurantDescription);
    }


    public void btnMakeReservation(View view) {
		
		//creating and initializing an object of intent and calling the MakeReservation class
        Intent reservation = new Intent(this, MakeReservation.class);
        reservation.putExtra(CommonTags.TAG_SELECTED_RESTAURANT, restaurantId);
        startActivity(reservation);//starting the activity
    }


    public void btnViewMenu(View view) {

		//creating and initializing an object of intent and calling the MealListActivity class
        Intent menu = new Intent(this, MealListActivity.class);
        menu.putExtra(CommonTags.TAG_SELECTED_RESTAURANT, restaurantId);
        startActivity(menu);

    }
}
