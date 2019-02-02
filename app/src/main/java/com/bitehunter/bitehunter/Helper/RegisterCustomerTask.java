package com.bitehunter.bitehunter.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.JSONParser;
import com.bitehunter.bitehunter.Others.RemoteURL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yash on 12/22/2017.
 */

public class RegisterCustomerTask extends AsyncTask<HashMap<String, String>, Void, String> {

    private View view;
    Context context;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog dialog;

    //adding the remote URL for the .php
    private static String url_register_user = RemoteURL.addCustomer;

    public RegisterCustomerTask(View view) {
        this.context = view.getContext();
        this.view = view;
    }
//functions of the dialog that shows when adding a new user
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Adding new User..");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected String doInBackground(HashMap<String, String>... args) {

        //instantiating a hash map to get the pointers
        HashMap<String, String> updateList = args[0];

        //Building parameters and submitting the data on to a list and updating the database
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", updateList.get("name")));
        params.add(new BasicNameValuePair("password", updateList.get("password")));
        params.add(new BasicNameValuePair("email", updateList.get("email")));
        params.add(new BasicNameValuePair("contact", updateList.get("contact")));


        //getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(url_register_user, params);
        String message = null;
        try {
            // Checking the log cat for JSON response
            Log.d("Response: ", json.toString());
            message = json.getString(CommonTags.TAG_MESSAGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    //after execution the functions that will happen
    @Override
    protected void onPostExecute(String result) {

        dialog.dismiss();
        CommonMethods.displayToast(context, result);
        ((Activity) (context)).finish();
    }
}

