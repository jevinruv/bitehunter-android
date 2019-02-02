package com.bitehunter.bitehunter.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bitehunter.bitehunter.Helper.RateAndReviewRetrieveTask;
import com.bitehunter.bitehunter.Others.PMSGrid;
import com.bitehunter.bitehunter.R;

public class RateAndReviewActivity extends AppCompatActivity {
    View view;
    private String RestID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_and_review);
        view = getWindow().getDecorView().findViewById(android.R.id.content);

        Bundle bundle = getIntent().getExtras();
        RestID = String.valueOf(bundle.getInt("RestID"));
        new RateAndReviewRetrieveTask(view,RestID).execute();

    }

}
