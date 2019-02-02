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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jevin on 27-Dec-17.
 */

public class HomePopulateTask extends AsyncTask<Void, Void, String> {

    private AsyncTaskCompleteListener<HashMap> listener;
    private static String url_getHomeActivityItems = RemoteURL.getHomeActivityItems;
    private final Context context;
    private final View view;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();


    HashMap<String, Object> homeActivityItems = new HashMap<>();

    List<Meal> mealList = new ArrayList<>();
    List<Restaurant> restaurantList = new ArrayList<>();

    JSONArray mealsJson = null;
    JSONArray restaurantsJson = null;

    public HomePopulateTask(View view) {
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

    protected String doInBackground(Void... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_getHomeActivityItems, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("HOME ACTIVITY ITEMS: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                mealsJson = json.getJSONArray(CommonTags.TAG_MEAL_LIST);
                restaurantsJson = json.getJSONArray(CommonTags.TAG_RESTAURANT_LIST);

                getJsonMeals(mealsJson);
                getJsonRestaurants(restaurantsJson);

                homeActivityItems.put(CommonTags.TAG_MEAL_LIST, mealList);
                homeActivityItems.put(CommonTags.TAG_RESTAURANT_LIST, restaurantList);

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
            listener.onTaskComplete(homeActivityItems);
        }
    }


    public void getJsonRestaurants(JSONArray restaurantsJson) throws JSONException {
        for (int i = 0; i < restaurantsJson.length(); i++) {

            JSONObject jsonObject = restaurantsJson.getJSONObject(i);

            Restaurant restaurant = new Restaurant();

            restaurant.setId(jsonObject.getInt("restaurant_id"));
            restaurant.setName(jsonObject.getString("restaurant_name"));
            restaurant.setImage(jsonObject.getString("restaurant_image"));
            restaurant.setTimeOpen(jsonObject.getString("time_open"));
            restaurant.setTimeClose(jsonObject.getString("time_close"));

            restaurantList.add(restaurant);
        }
    }

    public void getJsonMeals(JSONArray mealsJson) throws JSONException {
        for (int i = 0; i < mealsJson.length(); i++) {

            JSONObject jsonObject = mealsJson.getJSONObject(i);

            Meal meal = new Meal();

            meal.setItemID(jsonObject.getString("item_id"));
            meal.setName(jsonObject.getString("meal_name"));
            meal.setPrice(jsonObject.getString("meal_price"));
            meal.setImage(jsonObject.getString("meal_image"));

            mealList.add(meal);
        }
    }

}
