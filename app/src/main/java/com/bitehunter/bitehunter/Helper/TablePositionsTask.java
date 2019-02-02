package com.bitehunter.bitehunter.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.bitehunter.bitehunter.Model.TablePosition;
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
 * Created by Jevin on 23-Oct-17.
 */

public class TablePositionsTask extends AsyncTask<Integer, Void, String> {

    private AsyncTaskCompleteListener<HashMap> listener;
    private static String url_getTablePositions = RemoteURL.getTablePositions;
    private Context context;
    private View view;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    HashMap<String, TablePosition> tablePositions = new HashMap<>();
    JSONArray tablesJson = null;

    public TablePositionsTask(View view) {
        this.context = view.getContext();
        this.view = view;
        this.listener = (AsyncTaskCompleteListener) context;
    }

    // display progress dialog
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading Table Positions..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(Integer... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("restaurant_id", String.valueOf(args[0])));
        JSONObject json = jsonParser.makeHttpRequest(url_getTablePositions, params);

        try {
            // Check your log cat for JSON reponse
            Log.d("Table Positions: ", json.toString());

            if (json.getInt(CommonTags.TAG_SUCCESS) == 1) {

                tablesJson = json.getJSONArray(CommonTags.TAG_TABLE_DATA);

                for (int i = 0; i < tablesJson.length(); i++) {

                    JSONObject jsonObject = tablesJson.getJSONObject(i);

                    TablePosition tablePosition = new TablePosition();

                    tablePosition.setTable_id(jsonObject.getString("table_id"));
                    tablePosition.setLeft_Margin(jsonObject.getInt("left_margin"));
                    tablePosition.setTop_Margin(jsonObject.getInt("top_margin"));

                    tablePositions.put(tablePosition.getTable_id(), tablePosition);
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
    protected void onPostExecute(String result) {
        pDialog.dismiss();

        if (result != null) {
            //CommonMethods.displaySnackbar(view, text);
            CommonMethods.displayToast(context, result);
            ((Activity) (context)).finish();
        } else {
            listener.onTaskComplete(tablePositions);
        }
    }
}
