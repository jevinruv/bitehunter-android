package com.bitehunter.bitehunter.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.bitehunter.bitehunter.Model.Meal;
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
 * Created by Jevin on 27-Dec-17.
 */

public class RestaurantListTask extends AsyncTask<String, Void, String> {

    private final View view;
    private AsyncTaskCompleteListener<ArrayList> listener;
    private static String url_getRestaurantList = RemoteURL.getRestaurantList;
    private final Context context;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    private ArrayList<Restaurant> listRestaurant = new ArrayList<>();

    JSONArray restaurantJson = null;

    public RestaurantListTask(View view) {
        this.context = view.getContext();
        this.view = view;
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

    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_getRestaurantList, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("RESTAURANT LIST: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                restaurantJson = json.getJSONArray(CommonTags.TAG_RESTAURANT_LIST);
                getJsonRestaurants(restaurantJson);

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
            listener.onTaskComplete(listRestaurant);
        }
    }


    public void getJsonRestaurants(JSONArray mealsJson) throws JSONException {
        for (int i = 0; i < mealsJson.length(); i++) {

            JSONObject jsonObject = mealsJson.getJSONObject(i);

            Restaurant restaurant = new Restaurant();

            restaurant.setId(jsonObject.getInt(CommonTags.TAG_RESTAURANT_ID));
            restaurant.setName(jsonObject.getString(CommonTags.TAG_RESTAURANT_NAME));
            restaurant.setImage(jsonObject.getString(CommonTags.TAG_RESTAURANT_IMAGE));
            restaurant.setTimeOpen(jsonObject.getString(CommonTags.TAG_RESTAURANT_TIME_OPEN));
            restaurant.setTimeClose(jsonObject.getString(CommonTags.TAG_RESTAURANT_TIME_CLOSE));

            listRestaurant.add(restaurant);
        }
    }

}
