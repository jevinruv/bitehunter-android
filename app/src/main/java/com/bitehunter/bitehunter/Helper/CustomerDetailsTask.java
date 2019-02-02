package com.bitehunter.bitehunter.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.bitehunter.bitehunter.Model.Customer;
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
import java.util.List;

/**
 * Created by Jevin on 16-Nov-17.
 */

public class CustomerDetailsTask extends AsyncTask<String, Void, String> {

    private View view;
    private Context context;
    private AsyncTaskCompleteListener listener;
    private ProgressDialog pDialog;
    private static String url_get_customer = RemoteURL.getCustomer;
    JSONArray customerDataJson = null;
    Customer customer = new Customer();

    JSONParser jsonParser = new JSONParser();

    public CustomerDetailsTask(View view) {
        this.context = view.getContext();
        this.view = view;
        this.listener = (AsyncTaskCompleteListener) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Retrieving Customer details..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {

        String customerId = args[0];

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("customer_id", customerId));

        JSONObject json = jsonParser.makeHttpRequest(url_get_customer, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("Customer Details: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                customer = new Customer();
                customerDataJson = json.getJSONArray(CommonTags.TAG_CUSTOMER);

                for (int i = 0; i < json.length(); i++) {

                    JSONObject jsonObject = customerDataJson.getJSONObject(i);

                    customer.setId(jsonObject.getString("customer_id"));
                    customer.setName(jsonObject.getString("customer_name"));
                    customer.setEmail(jsonObject.getString("customer_email"));
                    customer.setContact(jsonObject.getString("customer_contact"));
                    customer.setPassword(jsonObject.getString("customer_password"));
                }

            } else if (json.getInt(CommonTags.TAG_SUCCESS) == 0) {
                return json.getString(CommonTags.TAG_MESSAGE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();

        if (result != null) {
            //CommonMethods.displaySnackbar(view, result);
            CommonMethods.displayToast(context, result);
            ((Activity) (context)).finish();

        } else {
            listener.onTaskComplete(customer);
        }
    }
}
