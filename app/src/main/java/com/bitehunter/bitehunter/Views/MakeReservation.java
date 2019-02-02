package com.bitehunter.bitehunter.Views;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.RestaurantsDetailsTask;
import com.bitehunter.bitehunter.Helper.RetrieveAllRestaurantsTask;
import com.bitehunter.bitehunter.Model.Restaurant;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeReservation extends BaseActivity implements AsyncTaskCompleteListener<HashMap>, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private View view;
    private Spinner spinnerRestaurant;
    private TextView labelOpenTime;
    private TextView labelCloseTime;
    private TextView labelStartTime;
    private TextView labelEndTime;
    RelativeLayout layoutTimeChoose;
    private Button btn_table;
    private Button btn_menu;

    HashMap<String, String> selectedMenuMealIdAndName = new HashMap<>();
    HashMap<String, Double> selectedMenuMealIdAndPrice = new HashMap<>();
    HashMap<Integer, Restaurant> restaurantDetails = new HashMap<>();
    HashMap<String, Object> reservationDetails = new HashMap<>();
    HashMap<String, Object> selectedMenu = new HashMap<>();

    public List<String> selectedMenuMealNames;
    public List<String> selectedMenuMealIds;

    private int selectedRestaurant;
    private String selectedTable;
    private String selectedStartTime;
    private String selectedEndTime;
    private String selectedMealIds;
    private String selectedMealNames;

    String restaurantOpenHours;
    String restaurantCloseHours;

    ArrayList<String> restaurantNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        view = getLayoutInflater().inflate(R.layout.activity_make_reservation, contentFrameLayout);

        toolbarTitle.setText("Reservation");

        this.deleteDatabase("bitehunter");
        initViews();
        //retrieving all the restaurant details in a list that are saved in the database
        new RetrieveAllRestaurantsTask(view).execute();

    }

    public void initViews() {
        /*
        retrieving view Ids from the layout file and assigning them to
        local variables
        */
        spinnerRestaurant = (Spinner) findViewById(R.id.spinner_restaurant);
        labelStartTime = (TextView) findViewById(R.id.label_select_start);
        labelEndTime = (TextView) findViewById(R.id.label_select_end);
        labelOpenTime = (TextView) findViewById(R.id.label_open_time);
        labelCloseTime = (TextView) findViewById(R.id.label_close_time);
        btn_table = (Button) findViewById(R.id.btn_table);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        layoutTimeChoose = (RelativeLayout) findViewById(R.id.layout_time_choose);

        //setting a button listener for the card view of time choose
        layoutTimeChoose.setOnClickListener(this);
        //setting a listener for the drop down menu
        spinnerRestaurant.setOnItemSelectedListener(this);
    }

    public void initSpinnerValues() {

        restaurantNames = new ArrayList<>();//creating an empty ArrayList to store the restaurant names
        restaurantNames.add("Select..");  //setting the first element of the ArrayList as "Select"

        /*retrieving all the restaurant names from the Task that was completed
        when it was executed to retrieve the restaurant details*/
        for (Map.Entry<Integer, Restaurant> entry : restaurantDetails.entrySet()) {
            Restaurant restaurant = entry.getValue();
            restaurantNames.add(restaurant.getName());
        }

        //creating an empty ArrayAdapter to assign it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, restaurantNames) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }
        };

        //setting the adapter with the restaurant names to the spinner
        spinnerRestaurant.setAdapter(adapter);
        spinnerRestaurant.setSelection(0, false);
    }

    public void timeChooseClicked() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    //when the user selects the time assign the times to the labels
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        selectedStartTime = String.format("%02d:%02d:%02d", hourOfDay, minute, 00);
                        labelStartTime.setText(selectedStartTime);
                        selectedEndTime = addEndTime();
                        labelEndTime.setText(selectedEndTime);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

        resetValues("table"); //resets selected table
    }

    /* Executed when the table button is clicked to choose the reservation table. */
    public void btnTableClicked(View view) {

        //if the user does not select a restaurant, display an error message
        if (selectedRestaurant == 0) {
            commonMethods.displaySnackbar(view, R.string.error_empty_field_restaurant);
        } else if (selectedStartTime == null) {
            commonMethods.displaySnackbar(view, R.string.error_empty_field_time);
        }
        //if the selected restaurant is not empty, pass the below variables to the SelectTable activity
        else {
            Intent intent = new Intent(this, SelectTable.class);
            intent.putExtra(CommonTags.TAG_SELECTED_RESTAURANT, selectedRestaurant);
            intent.putExtra(CommonTags.TAG_SELECTED_TABLE, selectedTable);
            intent.putExtra(CommonTags.TAG_SELECTED_START_TIME, selectedStartTime);
            intent.putExtra(CommonTags.TAG_SELECTED_END_TIME, selectedEndTime);
            intent.putExtra(CommonTags.TAG_TABLE_COUNT, restaurantDetails.get(selectedRestaurant).getTableCount());

            //once the user clicks the back button, the current activity will retrieve the selected table
            startActivityForResult(intent, 1);
        }
    }

    //when the customer clicks the check out button, the following method is run
    public void btnCheckoutClicked(View view) {

        		/*if the selected restaurant is not empty
        AND the start time of the restaurant is not null
		AND the end time of the restaurant is not null,
		the reservation details will be saved in the HashMap*/
        if ((selectedRestaurant != 0) && (selectedStartTime != null) && (selectedTable != null) && (selectedMealIds != null)) {

            reservationDetails.put(CommonTags.TAG_SELECTED_RESTAURANT, String.valueOf(selectedRestaurant));
            reservationDetails.put(CommonTags.TAG_SELECTED_START_TIME, selectedStartTime);
            reservationDetails.put(CommonTags.TAG_SELECTED_END_TIME, selectedEndTime);
            reservationDetails.put(CommonTags.TAG_SELECTED_MENU_MEAL_IDS, selectedMealIds);
            reservationDetails.put(CommonTags.TAG_SELECTED_MENU_MEAL_NAMES, selectedMealNames);
            reservationDetails.put(CommonTags.TAG_SELECTED_MENU_MEAL_ID_AND_NAME, selectedMenuMealIdAndName);
            reservationDetails.put(CommonTags.TAG_SELECTED_MENU_MEAL_ID_AND_PRICE, selectedMenuMealIdAndPrice);
            reservationDetails.put(CommonTags.TAG_SELECTED_TABLE, selectedTable);

            /*Once the details of the reservation are sent to the HashMap,
              the CheckOutActivity is executed*/
            Intent intentCheckout = new Intent(this, CheckOutActivity.class);
            intentCheckout.putExtra(CommonTags.TAG_RESERVATION_DETAILS, reservationDetails);
            startActivity(intentCheckout);

        }

        //if the reservation details are empty, an error message is displayed
        else {
            CommonMethods.displaySnackbar(view, "Reservation Details Incomplete");
        }
    }


    public void btnMenuClicked(View view) {

        //if the selected restaurant is empty, an error message is displayed
        if (selectedRestaurant == 0) {
            commonMethods.displaySnackbar(view, R.string.error_empty_field_restaurant);
        }
        //if the selected restaurant is not empty, RestaurantMenuActivity is executed
        else {
            Intent intentMenu = new Intent(this, RestaurantMenuActivity.class);
            intentMenu.putExtra(CommonTags.TAG_SELECTED_RESTAURANT, selectedRestaurant);
            startActivityForResult(intentMenu, 2);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if the requestCode is table select, get the selected table
        switch (requestCode) {

            //if the requestCode is 1, the selected table will be retrieved from here and assigned to the variables
            case 1: //table select
                if (resultCode == RESULT_OK) {
                    selectedTable = data.getStringExtra(CommonTags.TAG_SELECTED_TABLE);
                    btn_table.setText(selectedTable);
                }
                break;

            //if the requestCode is 2, the selected meals will be retrieved from here and assigned to variables
            case 2: //menu select
                if (resultCode == RESULT_OK) {

                    selectedMenu = (HashMap<String, Object>) data.getSerializableExtra(CommonTags.TAG_SELECTED_MENU);

                    selectedMenuMealIdAndName = (HashMap<String, String>) selectedMenu.get(CommonTags.TAG_SELECTED_MENU_MEAL_ID_AND_NAME);
                    selectedMenuMealIdAndPrice = (HashMap<String, Double>) selectedMenu.get(CommonTags.TAG_SELECTED_MENU_MEAL_ID_AND_PRICE);

                    selectedMealIds = TextUtils.join(", ", selectedMenuMealIdAndName.keySet());
                    selectedMealNames = TextUtils.join(", ", selectedMenuMealIdAndName.values());
                    btn_menu.setText(selectedMealNames);
                }
                break;
        }
    }

    //callback method once the RestaurantDetailsTask has been finished
    @Override
    public void onTaskComplete(HashMap result) {
        restaurantDetails = result;
        initSpinnerValues();

        //executed to check whether the restaurant id has been selected from the restaurant details activity page
        getSelectedData();
    }

    //to clear all the labels and the variables
    public void resetValues(String type) {

        switch (type) {

            /*if the type of label to be reset is time,
        the start time and the end time will be set to blank fields
		and the variables will also be resetted to null*/
            case "time":
                selectedStartTime = null;
                selectedEndTime = null;
                labelStartTime.setText("");
                labelEndTime.setText("");
                break;

            /*if the type of label to be reset is table,
        the selected table will be set to null
		and the variables will also be resetted to null*/
            case "table":
                selectedTable = null;
                btn_table.setText("");
                break;

            /*if the type of label to be reset is all,
        the start time, the end time and the selected table will be set to null
		and the variables will also be resetted to null*/
            case "all":
                selectedStartTime = null;
                selectedEndTime = null;
                selectedTable = null;
                labelStartTime.setText("");
                labelEndTime.setText("");
                btn_table.setText("");
                break;
        }
    }

    //automatically setting the end time once the user selects a start time
    //increments the start time by 1 hour
    public String addEndTime() {
        Date date = null;
        String addedTime = null;

        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            date = formatter.parse(selectedStartTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);

        addedTime = formatter.format(calendar.getTime());
        return addedTime.toString();
    }

    public void setRestaurantOpenHours() {

        //retrieving the time open of the selected restaurant
        restaurantOpenHours = restaurantDetails.get(selectedRestaurant).getTimeOpen();
        //retrieving the time close of the selected restaurant
        restaurantCloseHours = restaurantDetails.get(selectedRestaurant).getTimeClose();

        //setting the labels with the corresponding start time and close time of the selected restaurant
        labelOpenTime.setText(restaurantOpenHours);
        labelCloseTime.setText(restaurantCloseHours);

    }


    //methods for spinner (restaurant selection)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        if (position == 0) {    //selected restaurant resets to zero
            selectedRestaurant = position;
            Log.d("selectedRestaurant = ", selectedRestaurant + "");
        } else {

            try {
                String restaurantName = restaurantNames.get(position);
                searchSelectedRestaurantId(restaurantName);
                Log.d("selectedRestaurant = ", selectedRestaurant + restaurantDetails.get(selectedRestaurant).getName());

                setRestaurantOpenHours();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        resetValues("all");  //resets selected time and table
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //handle button clicks
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.layout_time_choose:
                timeChooseClicked();
                break;
        }
    }

    public void getSelectedData() {
        Intent intent = getIntent();

        //if the restaurant has been selected from the Restaurant Details page
        //assign the restaurant ID to the variables, labels and set the spinner
        if (intent.hasExtra(CommonTags.TAG_SELECTED_RESTAURANT)) {
            selectedRestaurant = intent.getIntExtra(CommonTags.TAG_SELECTED_RESTAURANT, 0);

            String selectedRestaurantName = restaurantDetails.get(selectedRestaurant).getName();
            int spinnerPosition = restaurantNames.indexOf(selectedRestaurantName);
            spinnerRestaurant.setSelection(spinnerPosition);
        }
    }


    public void searchSelectedRestaurantId(String restaurantName) {
        for (Restaurant restaurant : restaurantDetails.values()) {
            if (restaurant.getName().equals(restaurantName)) {
                selectedRestaurant = restaurant.getId();
            }
        }
    }


} //end make reservation class
