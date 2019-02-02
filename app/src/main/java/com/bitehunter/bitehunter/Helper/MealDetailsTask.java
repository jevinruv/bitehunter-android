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

public class MealDetailsTask extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener<HashMap> listener;
    private static String url_getMealDetails = RemoteURL.getMealDetails;
    private final Context context;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();


    HashMap<String, String> mealDetails = new HashMap<>();

    JSONArray mealJson = null;

    public MealDetailsTask(Context context) {
        this.context = context;
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

        String itemID = args[0];

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CommonTags.TAG_MEAL_ITEM_ID, itemID));

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_getMealDetails, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("MEAL DETAILS: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                mealJson = json.getJSONArray(CommonTags.TAG_MEAL_DETAILS);

                getJsonMeal(mealJson);

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
            listener.onTaskComplete(mealDetails);
        }

    }


    public void getJsonMeal(JSONArray mealJson) throws JSONException {
        for (int i = 0; i < mealJson.length(); i++) {

            JSONObject jsonObject = mealJson.getJSONObject(i);

            mealDetails.put(CommonTags.TAG_MEAL_ITEM_ID, jsonObject.getString(CommonTags.TAG_MEAL_ITEM_ID));
            mealDetails.put(CommonTags.TAG_MEAL_NAME, jsonObject.getString(CommonTags.TAG_MEAL_NAME));
            mealDetails.put(CommonTags.TAG_MEAL_PRICE, jsonObject.getString(CommonTags.TAG_MEAL_PRICE));
            mealDetails.put(CommonTags.TAG_MEAL_IMAGE, jsonObject.getString(CommonTags.TAG_MEAL_IMAGE));
            mealDetails.put(CommonTags.TAG_MEAL_DESCRIPTION, jsonObject.getString(CommonTags.TAG_MEAL_DESCRIPTION));
        }
    }

}
