package com.bitehunter.bitehunter.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
 * Created by Jevin on 19-Oct-17.
 */

public class RestaurantsDetailsTask extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener<HashMap> listener;
    private static String url_getRestaurantsDetails = RemoteURL.getGetRestaurantDetails;
    private View view;
    private final Context context;


    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    JSONArray restaurantJson = null;

    HashMap<String, String> restaurantDetails = new HashMap<>();

    public RestaurantsDetailsTask(View view) {
        this.context = view.getContext();
        this.listener = (AsyncTaskCompleteListener) context;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading Details..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {

        String restaurantId = args[0];

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CommonTags.TAG_RESTAURANT_ID, restaurantId));

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_getRestaurantsDetails, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("Restaurant DETAILS: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                restaurantJson = json.getJSONArray(CommonTags.TAG_RESTAURANT_DETAILS);

                getJsonRestaurant(restaurantJson);

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
            CommonMethods.displayAlertDialog(context, text);
        } else {
            listener.onTaskComplete(restaurantDetails);
        }

    }


    public void getJsonRestaurant(JSONArray restaurantJson) throws JSONException {
        for (int i = 0; i < restaurantJson.length(); i++) {

            JSONObject jsonObject = restaurantJson.getJSONObject(i);

            restaurantDetails.put(CommonTags.TAG_RESTAURANT_ID, String.valueOf(jsonObject.getInt(CommonTags.TAG_RESTAURANT_ID)));
            restaurantDetails.put(CommonTags.TAG_RESTAURANT_NAME, jsonObject.getString(CommonTags.TAG_RESTAURANT_NAME));
            restaurantDetails.put(CommonTags.TAG_RESTAURANT_IMAGE, jsonObject.getString(CommonTags.TAG_RESTAURANT_IMAGE));
            restaurantDetails.put(CommonTags.TAG_RESTAURANT_TABLE_COUNT, jsonObject.getString(CommonTags.TAG_RESTAURANT_TABLE_COUNT));
            restaurantDetails.put(CommonTags.TAG_RESTAURANT_TIME_OPEN, jsonObject.getString(CommonTags.TAG_RESTAURANT_TIME_OPEN));
            restaurantDetails.put(CommonTags.TAG_RESTAURANT_TIME_CLOSE, jsonObject.getString(CommonTags.TAG_RESTAURANT_TIME_CLOSE));
            restaurantDetails.put(CommonTags.TAG_RESTAURANT_DESCRIPTION, jsonObject.getString(CommonTags.TAG_RESTAURANT_DESCRIPTION));
        }
    }
}