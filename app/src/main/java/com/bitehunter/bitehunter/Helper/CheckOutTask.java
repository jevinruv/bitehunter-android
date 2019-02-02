package com.bitehunter.bitehunter.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.bitehunter.bitehunter.Model.Restaurant;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.JSONParser;
import com.bitehunter.bitehunter.Others.RemoteURL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jevin on 14-Jan-18.
 */

public class CheckOutTask extends AsyncTask<HashMap<String, Object>, Void, String> {

    private AsyncTaskCompleteListener<String> listener;
    private static String url_addReservationDetails = RemoteURL.addReservationDetails;
    private final Context context;
    private final View view;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();


    HashMap<String, Object> reservationDetails = new HashMap<>();

    HashMap<Integer, Restaurant> restaurantDetails = new HashMap<>();
    JSONArray reserJson = null;
    JSONObject json;

    private String selectedRestaurant;
    private String selectedTable;
    private String selectedStartTime;
    private String selectedEndTime;
    private String selectedMealIds;
    private String selectedMealNames;
    private String customerId;
    private String customerName;

    SharedPreferences sharedpreferences;


    public CheckOutTask(View view) {
        this.context = view.getContext();
        this.view = view;
        this.listener = (AsyncTaskCompleteListener) context;
    }


    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Creating a Reservation..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    protected String doInBackground(HashMap<String, Object>[] args) {

        reservationDetails = args[0];

        createReservationDetails();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CommonTags.TAG_CUSTOMER_ID, customerId));
        params.add(new BasicNameValuePair(CommonTags.TAG_CUSTOMER_NAME, customerName));
        params.add(new BasicNameValuePair(CommonTags.TAG_SELECTED_TABLE, selectedTable));
        params.add(new BasicNameValuePair(CommonTags.TAG_SELECTED_MENU_MEAL_IDS, selectedMealIds));
        params.add(new BasicNameValuePair(CommonTags.TAG_SELECTED_MENU_MEAL_NAMES, selectedMealNames));
        params.add(new BasicNameValuePair(CommonTags.TAG_SELECTED_RESTAURANT, selectedRestaurant));
        params.add(new BasicNameValuePair(CommonTags.TAG_SELECTED_START_TIME, selectedStartTime));
        params.add(new BasicNameValuePair(CommonTags.TAG_SELECTED_END_TIME, selectedEndTime));

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_addReservationDetails, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("Checkout Result: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                return json.getString(CommonTags.TAG_MESSAGE);

            } else {

                return json.getString(CommonTags.TAG_MESSAGE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String text) {
        pDialog.dismiss();

        if (text != null) {
            listener.onTaskComplete(text);
        }
    }

    public void createReservationDetails() {

        customerId = (String) reservationDetails.get(CommonTags.TAG_CUSTOMER_ID);
        customerName = (String) reservationDetails.get(CommonTags.TAG_CUSTOMER_NAME);
        selectedRestaurant = (String) reservationDetails.get(CommonTags.TAG_SELECTED_RESTAURANT);
        selectedStartTime = (String) reservationDetails.get(CommonTags.TAG_SELECTED_START_TIME);
        selectedEndTime = (String) reservationDetails.get(CommonTags.TAG_SELECTED_END_TIME);
        selectedMealIds = (String) reservationDetails.get(CommonTags.TAG_SELECTED_MENU_MEAL_IDS);
        selectedMealNames = (String) reservationDetails.get(CommonTags.TAG_SELECTED_MENU_MEAL_NAMES);
        selectedTable = (String) reservationDetails.get(CommonTags.TAG_SELECTED_TABLE);
    }


}
