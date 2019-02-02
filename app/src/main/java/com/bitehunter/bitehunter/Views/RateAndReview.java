package com.bitehunter.bitehunter.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitehunter.bitehunter.Helper.RateMealTask;
import com.bitehunter.bitehunter.Helper.RateRestaurantTask;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Pasan .M. Semage on 2017-12-09.
 */

public class RateAndReview {

    private View view;
    private Context context;

    private Dialog dialog;
    private Button btnSubmit;
    private RatingBar ratingBar;
    private EditText txtReview;
    private TextView txtUserName;

    SharedPreferences sharedPreferences;
    private String cusID;
    private String cusName;
    private String date;


    //In this constructor assign activity context to this class
    public RateAndReview(Context context) {
        this.context = context;
    }

    //this method used to create new dialog object using rate_restaurant XML file
    private void dialogInit() {

        //getting details from shared preference (Session handling information)
        getDetails();

        //initialize dialog object
        dialog = new Dialog(context);
        //request no title for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set the content view for dialog (rate_restaurant)
        dialog.setContentView(R.layout.rate_restaurant);
        //setting up dialog box background transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //setting up dialog can cancel by click on outer range of dialog
        dialog.setCancelable(true);

        //initialize rate_restaurant components in this class
        txtUserName = (TextView) dialog.findViewById(R.id.txtRUser);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        txtReview = (EditText) dialog.findViewById(R.id.txtReview);

    }

    //this method use for get Shared preference
    private void getDetails() {

        //initialize shared preference
        sharedPreferences = context.getSharedPreferences(CommonTags.MyPREFERENCES, Context.MODE_PRIVATE);
        //assign shared preference info to strings (Customer Id and Customer Name)
        cusID = sharedPreferences.getString(CommonTags.TAG_CUSTOMER_ID, null);
        cusName = sharedPreferences.getString(CommonTags.TAG_CUSTOMER_NAME, null);
    }

    //this method using for rate restaurant.
    //this method can call when customer need to review restaurant
    public void rateRestaurent(final String restID) {

        //calling dialogInit() method for create new dialog object
        dialogInit();

        //setup review text hint as "Type review about restaurant!"
        txtReview.setHint("Type review about restaurant!");
        //setup dialog object user name to customer name
        txtUserName.setText("Review by " + cusName);

        //What need to happen user click on Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking rate bar is select some rate or not
                if (ratingBar.getRating() == 0) {
                    //show message to customer to select rate for restaurant
                    Toast.makeText(context, "Please select rate for restaurant to submit", Toast.LENGTH_LONG).show();
                    //return nothing (Do not proceed)
                    return;
                }
                //if not execute RestaurantTask background worker for add new restaurant review
                else {
                    //getting data from dialog and assign it to string values
                    String rate = String.valueOf(ratingBar.getRating());
                    String review = txtReview.getText().toString();
                    //getting date
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    //call RateRestaurantTask to execute background work and add details to database
                    new RateRestaurantTask(context, cusID, cusName, restID, date, rate, review).execute();
                    //dismiss the dialog
                    dialog.dismiss();
                }


            }
        });

        //show the dialog
        dialog.show();
    }

    //this method using for rate meal.
    //this method can call when customer need to review meal
    public void rateMeal(final String itemID) {

        //calling dialogInit() method for create new dialog object
        dialogInit();
        //setup review text hint as "Type review about Meal!"
        txtReview.setHint("Type review about Meal!");
        //setup dialog object user name to customer name
        txtUserName.setText("Review by " + cusName);

        //What need to happen user click on Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking rate bar is select some rate or not
                if (ratingBar.getRating() == 0) {
                    //show message to customer to select rate for meal
                    Toast.makeText(context, "Please select rate for meal to submit", Toast.LENGTH_LONG).show();
                    //return nothing (Do not proceed)
                    return;

                }
                //if not execute RateMealTask background worker for add new restaurant review
                else {
                    //getting data from dialog and assign it to string values
                    String rate = String.valueOf(ratingBar.getRating());
                    String review = txtReview.getText().toString();
                    //getting date
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    //call RateMealTask to execute background work and add details to database
                    new RateMealTask(context, itemID, cusID, cusName, date, rate, review).execute();
                    //dismiss dialog
                    dialog.dismiss();

                }

            }
        });

        //show the dialog
        dialog.show();
    }
}
