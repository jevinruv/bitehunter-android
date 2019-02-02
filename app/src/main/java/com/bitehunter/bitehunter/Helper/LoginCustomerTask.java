package com.bitehunter.bitehunter.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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
 * Created by Yash on 12/20/2017.
 */

public class LoginCustomerTask extends AsyncTask<HashMap<String, String>, Void, String> {

    private AsyncTaskCompleteListener<HashMap> listener;
    private View view;
    Context context;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog dialog;

    JSONArray customerJson = null;

    HashMap<String, String> customerDetails;

    private String url_login_user = RemoteURL.loginCustomer;

    public LoginCustomerTask(View view) {
        this.context = view.getContext();
        this.view = view;
        this.listener = (AsyncTaskCompleteListener) context;
    }
//this shows the dialog and the functions that happens after clicking the logging in button
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Logging in..");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected String doInBackground(HashMap<String, String>... args) {

        HashMap<String, String> updateList = args[0];

        //Building parameters and putting the the information(password and email) got into an Array List
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("password", updateList.get("password")));
        params.add(new BasicNameValuePair("email", updateList.get("email")));

        String message = null;
        try {
            //getting JSON Object
            JSONObject json = jsonParser.makeHttpRequest(url_login_user, params);
            // Check your log cat for JSON response
            Log.d("MEAL DETAILS: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                customerDetails = new HashMap<>();
                customerJson = json.getJSONArray(CommonTags.TAG_CUSTOMER_DETAILS);

        //checking for the position on the list
                for (int i = 0; i < customerJson.length(); i++) {

        //getting the items from the JSONObject and putting them into strings (taking the id, name and the email)
                    JSONObject jsonObject = customerJson.getJSONObject(i);
                    customerDetails.put(CommonTags.TAG_CUSTOMER_ID, jsonObject.getString(CommonTags.TAG_CUSTOMER_ID));
                    customerDetails.put(CommonTags.TAG_CUSTOMER_NAME, jsonObject.getString(CommonTags.TAG_CUSTOMER_NAME));
                    customerDetails.put(CommonTags.TAG_CUSTOMER_EMAIL, jsonObject.getString(CommonTags.TAG_CUSTOMER_EMAIL));
                }

            } else {

                return json.getString(CommonTags.TAG_MESSAGE);
            }
        }

        //diagnosing an Exception and telling where and what in the code happened
        catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    //functions at the end of completion is mentioned here basically it will dismiss the dialog and put into customerDetails
    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        listener.onTaskComplete(customerDetails);

    }
}

