package com.bitehunter.bitehunter.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitehunter.bitehunter.Others.RemoteURL;
import com.bitehunter.bitehunter.Views.RateAndReviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pasan .M. Semage on 2018-01-24.
 */

public class RateRestaurantTask extends AsyncTask<String, Void, String> {

    private Context context;
    private View view;
    private ProgressDialog pDialog;
    private static final String url = RemoteURL.rate_restaurant;


    private String CusID;
    private String CusName;
    private String RestID;
    private String Date;
    private String Rate;
    private String Review;

    //initialize the class variables using constructor
    public RateRestaurantTask(Context context, String cusID, String cusName, String resID, String date, String rate, String review) {
        this.context = context;
        CusID = cusID;
        CusName = cusName;
        RestID = resID;
        Date = date;
        Rate = rate;
        Review = review;
    }

    //what will happen before background work execute
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //initialize ProgressDialog
        pDialog = new ProgressDialog(context);
        //Set message on ProgressDialog
        pDialog.setMessage("Adding Review...");
        //initialize loading amount is true
        pDialog.setIndeterminate(true);
        //set user cant cancel the progress dialog
        pDialog.setCancelable(false);
        //show progress dialog
        pDialog.show();
    }

    //what need to be done in background
    @Override
    protected String doInBackground(String... params) {

        //request string response from provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            //what need to happen when response is received
            public void onResponse(String response) {
                try {

                    //creating JSONObject to get response as JSON format
                    JSONObject rate = new JSONObject(response);

                    //check response and read data of it
                    //check whether data was insert or not
                    //if data was inserted into database JSon object response "success" will be 1 otherwise it will be 0
                    if (rate.getInt("success") == 1) {
                        //displaying review added
                        Toast.makeText(context,"Review Added",Toast.LENGTH_LONG).show();

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
                builder.setCancelable(true);
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
                params.put("CusID", CusID);
                params.put("CusName", CusName);
                params.put("RestID", RestID);
                params.put("Date", Date);
                params.put("Rate", Rate);
                params.put("Review", Review);

                //sending hash map generic to url as the parameters
                return params;
            }
        };

        //add request to the Volley queue
        Volley.newRequestQueue(context).add(stringRequest);
        //return null
        return null;
    }

    //what need to happen when background work is done
    @Override
    protected void onPostExecute(String s) {
        //dismiss the progress dialog
        pDialog.dismiss();
    }
}
