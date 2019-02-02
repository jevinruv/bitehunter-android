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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jevin on 31-Oct-17.
 */

public class TableReservedTask extends AsyncTask<HashMap<String, String>, Void, String> {

    private TableReservedTaskCompleteListener<List> listener;
    private static String url_getReservedTables = RemoteURL.getReservedTables;
    private Context context;
    private View view;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    List<String> reservedTables = new ArrayList<>();
    JSONArray reservedTablesJson = null;

    public TableReservedTask(View view) {
        this.context = view.getContext();
        this.view = view;
        //asssign callback
        this.listener = (TableReservedTaskCompleteListener) context;
    }


    // display progress dialog
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading Reserved Tables..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    protected String doInBackground(HashMap<String, String>... lists) {

        HashMap<String, String> stringList = lists[0];

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("restaurant_id", stringList.get("restaurant_id")));
        params.add(new BasicNameValuePair("checkin", stringList.get("checkin")));
        params.add(new BasicNameValuePair("checkout", stringList.get("checkout")));  //add 1hour to the checkin time

        JSONObject json = jsonParser.makeHttpRequest(url_getReservedTables, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("Reserved Tables: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                reservedTablesJson = json.getJSONArray(CommonTags.TAG_TABLE_LIST);

                for (int i = 0; i < reservedTablesJson.length(); i++) {

                    JSONObject jsonObject = reservedTablesJson.getJSONObject(i);
                    reservedTables.add(jsonObject.getString("table_id"));
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
            CommonMethods.displayToast(context, result);
            ((Activity) (context)).finish();

        } else {
            //callback and send result to activity
            listener.tableReservedOnTaskComplete(reservedTables);
        }
    }

}
