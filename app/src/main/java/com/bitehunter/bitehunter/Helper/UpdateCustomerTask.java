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
 * Created by Ludowica on 10/11/2017.
 */

public class UpdateCustomerTask extends AsyncTask<HashMap<String, String>, Void, String> {

    private View view;
    Context context;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog dialog;
    private static String url_update_customer = RemoteURL.updateCustomer;

    public UpdateCustomerTask(View view) {
        this.context = view.getContext();
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Updating customer details..");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected String doInBackground(HashMap<String, String>... args) {

        HashMap<String, String> updateList = args[0];

        //Building parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("customerId", updateList.get("customerId")));
        params.add(new BasicNameValuePair("name", updateList.get("name")));
        params.add(new BasicNameValuePair("password", updateList.get("password")));
        params.add(new BasicNameValuePair("email", updateList.get("email")));
        params.add(new BasicNameValuePair("contact", updateList.get("contact")));

        //getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(url_update_customer, params);
        String message = null;
        try {
            // Check your log cat for JSON reponse
            Log.d("Response: ", json.toString());
            message = json.getString(CommonTags.TAG_MESSAGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }


    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();

        //display toast message
        CommonMethods.displayToast(context, result);
        ((Activity) (context)).finish();
    }
}
