package com.bitehunter.bitehunter.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitehunter.bitehunter.Adapters.RateAdapter;
import com.bitehunter.bitehunter.Model.Rate;
import com.bitehunter.bitehunter.Others.PMSGrid;
import com.bitehunter.bitehunter.Others.RemoteURL;
import com.bitehunter.bitehunter.R;
import com.bitehunter.bitehunter.Views.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by Pasan .M. Semage on 2018-01-22.
 */

public class RateAndReviewRetrieveTask extends AsyncTask<String, Void, String> {

    private View view;
    private Context context;
    private ProgressDialog pDialog;
    private static final String url = RemoteURL.getRateAndReviewDetaills;
    public ArrayList<Rate> list;
    private RateAdapter adapter;
    private PMSGrid grid;
    private String restaurantID;
    private int[] rateCount = new int[5];
    private int countReviews = 0;
    float avarageRate;
    TextView txtFiveCout, txtFourCount, txtThreeCount, txtTwoCount, txtOneCount,txtRate, txtCountOfReviewer;
    ProgressBar pgrFive, pgrFour, pgrThree, pgrTwo, pgrOne;
    RatingBar ratingBar;



    public RateAndReviewRetrieveTask(View view, String RestID) {
        this.view = view;
        this.context = view.getContext();
        this.restaurantID = RestID;

        grid = (PMSGrid) view.findViewById(R.id.reviewGrid);
        txtFiveCout = (TextView) view.findViewById(R.id.txtFiveCout);
        txtFourCount = (TextView) view.findViewById(R.id.txtFourCount);
        txtThreeCount = (TextView) view.findViewById(R.id.txtThreeCount);
        txtTwoCount = (TextView) view.findViewById(R.id.txtTwoCount);
        txtOneCount = (TextView) view.findViewById(R.id.txtOneCount);
        pgrFive = (ProgressBar) view.findViewById(R.id.pgrFive);
        pgrFour = (ProgressBar) view.findViewById(R.id.pgrFour);
        pgrThree = (ProgressBar) view.findViewById(R.id.pgrThree);
        pgrTwo = (ProgressBar) view.findViewById(R.id.pgrTwo);
        pgrOne = (ProgressBar) view.findViewById(R.id.pgrOne);
        txtRate = (TextView)view.findViewById(R.id.txtRating);
        txtCountOfReviewer = (TextView)view.findViewById(R.id.txtCountOfReviewers);
        ratingBar = (RatingBar) view.findViewById(R.id.rateBar);

        grid.setExpanded(true);
        grid.setFocusable(false);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.list = new ArrayList<>();
        adapter = new RateAdapter(view.getContext(), R.layout.rateandreview, list);

        for (int i = 0; i < rateCount.length; i++) {
            rateCount[i] = 0;
        }
    }

    @Override
    protected String doInBackground(String... params) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    if (array.length() == 0){
                        Toast.makeText(context,"No Reviews, Add review and be the first!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject rate = array.getJSONObject(i);

                            int r = rate.getInt("rate");
                            countReviews++;

                            list.add(new Rate(
                                    rate.getInt("reviewID"),
                                    rate.getInt("cusID"),
                                    rate.getString("cusName"),
                                    rate.getString("date"),
                                    rate.getInt("rate"),
                                    rate.getString("review")
                            ));

                            switch (r) {
                                case 5:
                                    rateCount[4] += 1;
                                    break;
                                case 4:
                                    rateCount[3] += 1;
                                    break;
                                case 3:
                                    rateCount[2] += 1;
                                    break;
                                case 2:
                                    rateCount[1] += 1;
                                    break;
                                case 1:
                                    rateCount[0] += 1;
                                    break;
                            }
                        }

                        avarageRate = (((5 * rateCount[4]) + (4 * rateCount[3]) + (3 * rateCount[2]) + (2 * rateCount[1]) + (1 * rateCount[0]))/countReviews);
                        txtRate.setText(Float.toString(avarageRate));
                        txtCountOfReviewer.setText(Integer.toString(countReviews));
                        ratingBar.setRating(avarageRate);


                        txtFiveCout.setText(Integer.toString(rateCount[4]));
                        txtFourCount.setText(Integer.toString(rateCount[3]));
                        txtThreeCount.setText(Integer.toString(rateCount[2]));
                        txtTwoCount.setText(Integer.toString(rateCount[1]));
                        txtOneCount.setText(Integer.toString(rateCount[0]));

                        pgrFive.setMax(countReviews);
                        pgrFour.setMax(countReviews);
                        pgrThree.setMax(countReviews);
                        pgrTwo.setMax(countReviews);
                        pgrOne.setMax(countReviews);


                        pgrFive.setProgress(rateCount[4]);
                        pgrFour.setProgress(rateCount[3]);
                        pgrThree.setProgress(rateCount[2]);
                        pgrTwo.setProgress(rateCount[1]);
                        pgrOne.setProgress(rateCount[0]);

                        grid.setAdapter(adapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error on response");
                builder.setMessage(error.getMessage());
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //need to change this intent later
                        Intent intent = new Intent(context, HomeActivity.class);
                        context.startActivity(intent);
                        ((Activity) (context)).finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("restID", restaurantID);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        pDialog.dismiss();

    }
}
