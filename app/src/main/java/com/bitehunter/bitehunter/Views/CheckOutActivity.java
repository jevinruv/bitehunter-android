package com.bitehunter.bitehunter.Views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.CheckOutTask;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;

import java.util.HashMap;

public class CheckOutActivity extends BaseActivity implements AsyncTaskCompleteListener<String> {

    private View view;
    EditText inputCardNumber;
    TextView labelTotal;
    Button btnDone;
    Context context;

    String validCardNumber = "0123-3456-6789-9123";

    HashMap<String, String> selectedMenuMealIdAndName = new HashMap<>();
    HashMap<String, Double> selectedMenuMealIdAndPrice = new HashMap<>();
    HashMap<String, Object> reservationDetails = new HashMap<>();

    private String selectedRestaurant;
    private String selectedTable;
    private String selectedTime;
    private String selectedMealIds;
    private String selectedMealNames;

    Double totalBill = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        view = getLayoutInflater().inflate(R.layout.activity_check_out, contentFrameLayout);
        context = this;

        toolbarTitle.setText("Checkout");

        getIntentExtras();

        initViews();
        setValues();
    }

    public void initViews() {

        labelTotal = (TextView) findViewById(R.id.label_total);
        inputCardNumber = (EditText) findViewById(R.id.input_card_number);
        btnDone = (Button) findViewById(R.id.btn_done);

        inputCardNumber.addTextChangedListener(new TextWatcher() {
            boolean isEdiging;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEdiging) return;
                isEdiging = true;
                // removing old dashes
                StringBuilder sb = new StringBuilder();
                sb.append(s.toString().replace("-", ""));

                if (sb.length() > 4)
                    sb.insert(4, "-");
                if (sb.length() > 9)
                    sb.insert(9, "-");
                if (sb.length() > 14)
                    sb.insert(14, "-");
                if (sb.length() > 19)
                    sb.delete(19, sb.length());

                s.replace(0, s.length(), sb.toString());
                isEdiging = false;
            }


        });
    }

    public void btnDoneClicked(View view) {

        String cardNumber = inputCardNumber.getText().toString();

        if (cardNumber.isEmpty()) {
            CommonMethods.displaySnackbar(view, "Card Number Field is Empty");
        } else if (validCardNumber.equals(cardNumber)) {

            createReservation();

        } else {
            CommonMethods.displaySnackbar(view, "Invalid Card Number");
        }
    }

    public void getIntentExtras() {

        Intent intent = getIntent();
        reservationDetails = (HashMap<String, Object>) intent.getSerializableExtra(CommonTags.TAG_RESERVATION_DETAILS);

        selectedRestaurant = (String) reservationDetails.get(CommonTags.TAG_SELECTED_RESTAURANT);
        selectedTime = (String) reservationDetails.get(CommonTags.TAG_SELECTED_START_TIME);
        selectedMealIds = (String) reservationDetails.get(CommonTags.TAG_SELECTED_MENU_MEAL_IDS);
        selectedMealNames = (String) reservationDetails.get(CommonTags.TAG_SELECTED_MENU_MEAL_NAMES);
        selectedMenuMealIdAndName = (HashMap<String, String>) reservationDetails.get(CommonTags.TAG_SELECTED_MENU_MEAL_ID_AND_NAME);
        selectedMenuMealIdAndPrice = (HashMap<String, Double>) reservationDetails.get(CommonTags.TAG_SELECTED_MENU_MEAL_ID_AND_PRICE);
        selectedTable = (String) reservationDetails.get(CommonTags.TAG_SELECTED_TABLE);
    }

    public void setValues() {
        for (Double price : selectedMenuMealIdAndPrice.values()) {
            totalBill += price;
        }

        labelTotal.setText("Rs. " + totalBill.toString());
    }

    public void createReservation() {

        SharedPreferences sharedPreferences = getSharedPrefs();
        String customerId = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_ID, null);
        String customerName = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_NAME, null);

        reservationDetails.put(CommonTags.TAG_CUSTOMER_ID, customerId);
        reservationDetails.put(CommonTags.TAG_CUSTOMER_NAME, customerName);

        new CheckOutTask(view).execute(reservationDetails);
    }

    @Override
    public void onTaskComplete(String message) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent home = new Intent(context, HomeActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(home);
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        this.deleteDatabase("bitehunter");

    }

}
