package com.bitehunter.bitehunter.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.JSONParser;
import com.bitehunter.bitehunter.Others.RemoteURL;
import com.bitehunter.bitehunter.Views.LoginActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Pasan .M. Semage on 2017-12-30.
 */

public class UnregisterUserTask extends AsyncTask<Void, Void, String> {

    private View view;
    private Context context;
    private ProgressDialog pDialog;
    private static final String url = RemoteURL.unregisterCustomer;
    private static String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    private String userID = null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    //initialize class variables by using constructor
    public UnregisterUserTask(View view, String userID) {
        this.view = view;
        this.context = view.getContext();
        this.userID = userID;
    }

    //what will happen before background work execute
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //initialize ProgressDialog
        pDialog = new ProgressDialog(context);
        //Set message on ProgressDialog
        pDialog.setMessage("Deactivating Account");
        //initialize loading amount is true
        pDialog.setIndeterminate(true);
        //set user cant cancel the progress dialog
        pDialog.setCancelable(false);
        //show progress dialog
        pDialog.show();
    }

    //what need to be done in background
    @Override
    protected String doInBackground(Void... params) {

        //initialize String variable called success
        String success = null;

        //initialize Array list to add parameters
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        //adding parameter
        param.add(new BasicNameValuePair("userID", userID));

        try {
            //initialize JSONObject to get request from url
            JSONObject json = jsonParser.makeHttpRequest(url, param);

            //assign success variable JSONObject data
            success = json.getString(TAG_SUCCESS);

        }
        //catching JSONExceptions (Json object errors)
        catch (JSONException e) {

            //print error to stack trace for debugging purposes
            e.printStackTrace();
        }

        //return success variable
        return success;
    }

    //what need to happen when background work is done
    protected void onPostExecute(String result) {

        //progress dialog dismiss
        pDialog.dismiss();

        //build alert dialog with passing context of activity
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //set alert dialog title
        builder.setTitle("Account");
        //set alert dialog message
        builder.setMessage("Your Account Successfully Deactivated...");
        //set alert dialog cannot cancel by user
        builder.setCancelable(false);
        //set up positive button and add on click listener to it
        //when user click on alert dialog OK button this will be execute
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //clearing shared preferences
                //for logout from the account
                //initialize shared preference
                sharedpreferences = context.getSharedPreferences(CommonTags.MyPREFERENCES, Context.MODE_PRIVATE);
                //getting editor permission to change shared preference
                editor = sharedpreferences.edit();
                //clear shared preference
                editor.clear();
                //save what did
                editor.commit();


                //create new intent to another activity (LoginActivity)
                Intent intent = new Intent(context, LoginActivity.class);
                //clearing back button stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //set intent act as new task
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //starting activity
                context.startActivity(intent);
                //finish activity (user cannot go back to this activity by clicking back button on mobile)
                ((Activity) (context)).finish();

            }
        });

        //create build alert dialog
        AlertDialog alertDialog = builder.create();
        //show alert dialog
        alertDialog.show();

    }
}
