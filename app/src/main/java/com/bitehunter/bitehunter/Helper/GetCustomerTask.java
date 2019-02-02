package com.bitehunter.bitehunter.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.JSONParser;
import com.bitehunter.bitehunter.Others.RemoteURL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ludowica on 16/11/2017.
 */

public class GetCustomerTask extends AsyncTask<HashMap<String, String>, Void, String> {
    private AsyncTaskCompleteListener<List> listener;
    private static String url_get_user_details = RemoteURL.getCustomer;
    private Context context;
    private View view;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    List<String> userDetails = new ArrayList<>();
    JSONArray userDetailsJson = null;

    public GetCustomerTask(AsyncTaskCompleteListener<List> listener, Context context, View view) {
        this.listener = listener;
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading Customer Details..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(HashMap<String, String>... lists) {
        HashMap<String, String> stringList = lists[0];

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nic", stringList.get("nic")));
        params.add(new BasicNameValuePair("name", stringList.get("name")));
        params.add(new BasicNameValuePair("password", stringList.get("password")));
        params.add(new BasicNameValuePair("email", stringList.get("email")));
        params.add(new BasicNameValuePair("contact", stringList.get("contact")));

        JSONObject json = jsonParser.makeHttpRequest(url_get_user_details, params);

        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        pDialog.dismiss();

        if(s != null){
            CommonMethods.displayToast(context, s);
            ((Activity) (context)).finish();
        }

        else{
            listener.onTaskComplete(userDetails);
        }
    }
}
