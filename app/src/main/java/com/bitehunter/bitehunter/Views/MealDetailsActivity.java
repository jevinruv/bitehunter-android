package com.bitehunter.bitehunter.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.MealDetailsTask;
import com.bitehunter.bitehunter.Helper.MealRateReviewRetrieveTask;
import com.bitehunter.bitehunter.Model.MealRate;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.CustomVolleyRequest;
import com.bitehunter.bitehunter.R;

import java.util.HashMap;

public class MealDetailsActivity extends AppCompatActivity implements AsyncTaskCompleteListener<HashMap> {

    private View view;
    ImageView mealImageExpanded;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NestedScrollView contentMealDetails;
    private ImageLoader imageLoader;
    TextView price;
    TextView description;
    Button btnWriteReviewMeal;

    HashMap<String, String> mealDetails = new HashMap<>();
    String itemID;

    //this one
    EditText txtMealRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        view = getWindow().getDecorView().findViewById(android.R.id.content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();


        itemID = getIntent().getExtras().getString(CommonTags.TAG_MEAL_ITEM_ID);
        new MealDetailsTask(MealDetailsActivity.this).execute(itemID);

        //Getting user review about the menu item
        new MealRateReviewRetrieveTask(view, itemID).execute();

        //initialize what happen when user click on write a review button
        rateAndReview();
    }

    private void rateAndReview() {
        //setup click listener
        btnWriteReviewMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create RateAndReview object and pass this activity context
                RateAndReview review = new RateAndReview(MealDetailsActivity.this);
                //calling rateMeal() function to rate and review food item
                review.rateMeal(itemID);
            }
        });
    }

    public void initViews() {
		//getting values from the layout file and assigning them to variables
        mealImageExpanded = (ImageView) findViewById(R.id.meal_image_expanded);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        contentMealDetails = (NestedScrollView) findViewById(R.id.content_meal_details);
        price = (TextView) contentMealDetails.findViewById(R.id.price);
        description = (TextView) contentMealDetails.findViewById(R.id.desc);
        btnWriteReviewMeal = (Button) contentMealDetails.findViewById(R.id.btnWriteReviewMeal);

        //this one added because every time the application is run keyboard is showed up.
        txtMealRequest = (EditText) contentMealDetails.findViewById(R.id.txtMealRequest);
        txtMealRequest.setFocusable(false);

    }

    public void setValues() {
		//setting values to String variables once they have been retrieved from the mealDetails HashMap
        String mealName = mealDetails.get(CommonTags.TAG_MEAL_NAME);
        String mealImage = mealDetails.get(CommonTags.TAG_MEAL_IMAGE);
        String mealPrice = mealDetails.get(CommonTags.TAG_MEAL_PRICE);
        String mealDescription = mealDetails.get(CommonTags.TAG_MEAL_DESCRIPTION);

        collapsingToolbarLayout.setTitle(mealName);

        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();

        imageLoader.get(mealImage, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                mealImageExpanded.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

		//setting the labels for price and description
        price.setText(mealPrice + ".00");
        description.setText(mealDescription);
    }

    @Override
    public void onTaskComplete(HashMap result) {
        mealDetails = result;
        setValues();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
