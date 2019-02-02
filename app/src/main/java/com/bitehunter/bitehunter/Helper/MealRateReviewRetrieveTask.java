package com.bitehunter.bitehunter.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitehunter.bitehunter.Adapters.MealRateAdapter;
import com.bitehunter.bitehunter.Model.MealRate;
import com.bitehunter.bitehunter.Others.PMSGrid;
import com.bitehunter.bitehunter.Others.RemoteURL;
import com.bitehunter.bitehunter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pasan .M. Semage on 2018-01-23.
 */

public class MealRateReviewRetrieveTask extends AsyncTask<String, Void, String> {

    private View view;
    private Context context;
    private String ItemID;
    private static final String url = RemoteURL.getMealRateAndReview;
    private ProgressDialog pDialog;

    private MealRateAdapter adapter;
    private ArrayList<MealRate> list;
    private int[] rateCount = new int[5];
    private int countReviews = 0;
    float avarage;

    TextView AvarageRate;
    RatingBar RatingMealAvarage;
    PMSGrid grid;

    //initialize class variables by using constructor
    public MealRateReviewRetrieveTask(View view, String ItemID) {
        this.view = view;
        this.ItemID = ItemID;
        this.context = view.getContext();

        AvarageRate = (TextView)view.findViewById(R.id.mealAvarageRate);
        RatingMealAvarage = (RatingBar)view.findViewById(R.id.ratingMealAvarage);
        grid = (PMSGrid)view.findViewById(R.id.mealsGrid);
        grid.setExpanded(true);
        grid.setFocusable(false);

    }

    //what will happen before background work execute
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //initialize ArrayList generics
        this.list = new ArrayList<>();
        //initializing MealRateAdapter class passing context and layout id and ArrayList
        adapter = new MealRateAdapter(view.getContext(), R.layout.rateandreview, list);

        //using for loop all the rateCount array list value assign to 0
        for (int i = 0; i < rateCount.length; i++) {
            //assigning 0 to array list rateCount
            rateCount[i] = 0;
        }
    }

    //what need to be done in background
    @Override
    protected String doInBackground(String... params) {

        //request string response from provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    try {
                        //initialize JSONArray to store response
                        JSONArray array = new JSONArray(response);

                        //checking array is empty or not
                        //if array is empty
                        if (array.length() == 0){
                            //average rate will be shown as N/A
                            AvarageRate.setText("N/A");
                            //return nothing (Do not proceed execution)
                            return;
                        }
                        //if array is not empty then,
                        else {
                            //using for loop to get every JSONObject
                            for (int i = 0; i < array.length(); i++) {

                                //declare and initialize JSONObject
                                JSONObject rate = array.getJSONObject(i);

                                //get Rate
                                int r = rate.getInt("mRate");
                                //increase countReviews (this use for get reviews count)
                                countReviews++;

                                //adding MealRate objects to the array list
                                list.add(new MealRate(
                                        rate.getInt("mCusID"),
                                        rate.getString("mCusName"),
                                        rate.getString("mDate"),
                                        rate.getInt("mRate"),
                                        rate.getString("mReview")
                                ));

                                //checking rate for count 5 stars rate count, 4 stars rate count,.......1 star rate count
                                switch (r) {
                                    case 5:
                                        rateCount[4] += 1;
                                        break;
                                    case 4:
                                        rateCount[3] += 1;
                                        break;
                                    case 3:
                                        rateCount[2] += 1;
                                        break;
                                    case 2:
                                        rateCount[1] += 1;
                                        break;
                                    case 1:
                                        rateCount[0] += 1;
                                        break;
                                }
                            }

                            //initialize average rate for the meal product
                            avarage = (((5 * rateCount[4]) + (4 * rateCount[3]) + (3 * rateCount[2]) + (2 * rateCount[1]) + (1 * rateCount[0]))/countReviews);
                            //set text average rate
                            AvarageRate.setText(Float.toString(avarage));
                            // set average rating bar rating
                            RatingMealAvarage.setRating(avarage);
                            //set grid adapter as adapter(MealRateAdapter)
                            grid.setAdapter(adapter);

                        }


                    }
                    //catching JSONExceptions (Json object errors)
                    catch (JSONException e) {

                        //print error to stack trace for debugging purposes
                        e.printStackTrace();
                    }



            }
        },
                //when errors on response what need to be happen
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //building alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //set title of alert dialog
                builder.setTitle("Error");
                //set message of alert dialog (error response message)
                builder.setMessage(error.getMessage());
                //set alert dialog cant cancel by clicking dialog outer range
                builder.setCancelable(false);
                //creating build alert dialog
                AlertDialog alertDialog = builder.create();
                //showing alert dialog
                alertDialog.show();

                //print error to stack trace for debugging purpose
                error.printStackTrace();
            }
        }) {
            //passing parameters to the URL
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //initialize HashMap generic
                Map<String, String> params = new HashMap<>();

                //putting all the parameters to hash map generic
                params.put("itemID", ItemID);

                //sending hash map generic to url as the parameters
                return params;
            }
        };

        //add request to the Volley queue
        Volley.newRequestQueue(context).add(stringRequest);
        //return null
        return null;
    }
}
