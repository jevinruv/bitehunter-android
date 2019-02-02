package com.bitehunter.bitehunter.Views;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.TablePositionsTask;
import com.bitehunter.bitehunter.Helper.TableReservedTask;
import com.bitehunter.bitehunter.Helper.TableReservedTaskCompleteListener;
import com.bitehunter.bitehunter.Model.TablePosition;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectTable extends BaseActivity implements
        View.OnClickListener, AsyncTaskCompleteListener<HashMap>, TableReservedTaskCompleteListener<List> {

    RelativeLayout mainLayout;
    int pixels;
    private int selectedRestaurant = 0;
    private String selectedTable = null;
    private String selectedStartTime = null;
    private String selectedEndTime = null;
    private int tableCount = 0;
    private View view;
    HashMap<String, CheckBox> restaurantTables = new HashMap<String, CheckBox>();
    HashMap<String, TablePosition> tablePositions = new HashMap<>();
    HashMap<String, String> reservationDetails = new HashMap<>();
    List<String> reservedTables = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        view = getLayoutInflater().inflate(R.layout.activity_select_table, contentFrameLayout);
        getSupportActionBar().hide();
        mainLayout = (RelativeLayout) findViewById(R.id.main);

        getIntentExtra();

        new TablePositionsTask(view).execute(selectedRestaurant);

        pixels = (int) (60 * this.getResources().getDisplayMetrics().density);
    }

    public static int val(int px) {
        return (int) (px * Resources.getSystem().getDisplayMetrics().density);
        //return px;
    }

    public void setCheckBoxPositions() {
        //assign checkbox ids and put in hashmap
        for (int i = 1; i <= tableCount; i++) {
            TablePosition tablePosition = tablePositions.get("t" + i);
            initCheckbox(i, tablePosition.getLeft_Margin(), tablePosition.getTop_Margin());
        }

        //if was table selected previously
        if (selectedTable != null) {
            restaurantTables.get(selectedTable).setChecked(true);
        }
    }

    public void initCheckbox(int id, int leftMargin, int topMargin) {

        CheckBox checkBox = new CheckBox(this);
        String checkBoxID = "t" + id;
        checkBox.setTag(checkBoxID);
        checkBox.setText(id + "");
        checkBox.setBackgroundResource(R.drawable.table_type1_checkbox);
        checkBox.setButtonDrawable(null);
        checkBox.setOnClickListener(this);
        checkBox.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        rel_btn.leftMargin = val(leftMargin);
        rel_btn.topMargin = val(topMargin);

        rel_btn.width = (pixels / 2);
        rel_btn.height = pixels;

        checkBox.setLayoutParams(rel_btn);
        mainLayout.addView(checkBox);

        restaurantTables.put(checkBoxID, checkBox);
    }

    @Override
    public void onClick(View v) {

        String table = (String) v.getTag();

        for (Map.Entry<String, CheckBox> entry : restaurantTables.entrySet()) {
            entry.getValue().setChecked(false);
        }
        restaurantTables.get(table).setChecked(true);
        selectedTable = table;
        //restaurantTables.get(t).setEnabled(false);
    }

    //table Positions task
    @Override
    public void onTaskComplete(HashMap result) {
        tablePositions = result;
        setCheckBoxPositions();
        // setCheckBoxReserved();   //waits till the tables are been initialized

        reservationDetails.put("restaurant_id", String.valueOf(selectedRestaurant));
        reservationDetails.put("checkin", selectedStartTime);
        reservationDetails.put("checkout", selectedEndTime);

        new TableReservedTask(view).execute(reservationDetails);
    }

    //get reserved table task
    @Override
    public void tableReservedOnTaskComplete(List result) {

        if (!result.isEmpty()) {
            reservedTables = result;
            setCheckBoxReserved();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(CommonTags.TAG_SELECTED_TABLE, selectedTable);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setCheckBoxReserved() {

        for (String table : reservedTables) {
            CheckBox checkBox = restaurantTables.get(table);
            checkBox.setBackgroundResource(R.drawable.table_type1_reserved);
            checkBox.setClickable(false);
        }
    }

    public void getIntentExtra() {
        selectedRestaurant = getIntent().getIntExtra(CommonTags.TAG_SELECTED_RESTAURANT, 0);
        selectedStartTime = getIntent().getStringExtra(CommonTags.TAG_SELECTED_START_TIME);
        selectedEndTime = getIntent().getStringExtra(CommonTags.TAG_SELECTED_END_TIME);
        selectedTable = getIntent().getStringExtra(CommonTags.TAG_SELECTED_TABLE);
        tableCount = getIntent().getIntExtra(CommonTags.TAG_TABLE_COUNT, 0);
    }

}
