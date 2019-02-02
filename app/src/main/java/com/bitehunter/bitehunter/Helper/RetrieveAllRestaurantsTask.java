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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jevin on 19-Oct-17.
 */

public class RetrieveAllRestaurantsTask extends AsyncTask<Void, Void, String> {

    private AsyncTaskCompleteListener<HashMap> listener;
    private static String url_getRestaurants = RemoteURL.getRestaurants;
    private View view;
    private final Context context;


    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    HashMap<Integer, Restaurant> restaurantDetails = new HashMap<>();
    JSONArray restaurantsJson = null;

    public RetrieveAllRestaurantsTask(View view) {
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
    protected String doInBackground(Void... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_getRestaurants, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("All restaurants: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                restaurantsJson = json.getJSONArray(CommonTags.TAG_RESTAURANTS);

                for (int i = 0; i < restaurantsJson.length(); i++) {

                    JSONObject jsonObject = restaurantsJson.getJSONObject(i);

                    Restaurant restaurant = new Restaurant();

                    restaurant.setId(jsonObject.getInt("restaurant_id"));
                    restaurant.setName(jsonObject.getString("restaurant_name"));
                    restaurant.setTableCount(jsonObject.getInt("table_count"));
                    restaurant.setTimeOpen(jsonObject.getString("time_open"));
                    restaurant.setTimeClose(jsonObject.getString("time_close"));

                    restaurantDetails.put(restaurant.getId(), restaurant);
                }

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
            // CommonMethods.displaySnackbar(view, text);
        } else {
            listener.onTaskComplete(restaurantDetails);
        }
    }
}