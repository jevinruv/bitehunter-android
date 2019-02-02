package com.bitehunter.bitehunter.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.CustomerDetailsTask;
import com.bitehunter.bitehunter.Helper.UnregisterUserTask;
import com.bitehunter.bitehunter.Helper.UpdateCustomerTask;
import com.bitehunter.bitehunter.Model.Customer;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.R;

import java.util.HashMap;

public class UpdateCustomerActivity extends BaseActivity implements AsyncTaskCompleteListener<Customer> {

    private View view;
    private EditText name;
    private EditText password;
    private EditText email;
    private EditText contact;
    private TextView labelName;

    private Button btnDeactivate;
    HashMap<String, String> updatedDetails = new HashMap<>();

    private String updatedName;
    private String updatedPassword;
    private String updatedEmail;
    private String updatedContact;
    SharedPreferences sharedpreferences;

    private String customerId;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        view = getLayoutInflater().inflate(R.layout.activity_update_customer, contentFrameLayout);

        toolbarTitle.setText("Manage Account");

        //the following methods are called when the activity begins
        getSharedPrefs();
        initViews();

        //the CustomerDetailsTask is executed by passing the customerId
        new CustomerDetailsTask(view).execute(customerId);

    }

    /*The details that are displaying in the Update Customer activity are retrieved
    and assigned to the following EditText variables The data is converted into EditText before assigning them
    to the variables.*/
    public void initViews() {
        name = (EditText) findViewById(R.id.input_name);
        password = (EditText) findViewById(R.id.input_password);
        email = (EditText) findViewById(R.id.input_email);
        contact = (EditText) findViewById(R.id.input_contact);
        labelName = (TextView) findViewById(R.id.label_main_name);
    }

    /*Validations methods are called to check whether the updated data values are
      valid.*/
    public void validate() {

        if (!CommonMethods.isEmailValid(updatedEmail)) {
            CommonMethods.displaySnackbar(view, R.string.error_invalid_email);
        } else if (!CommonMethods.isNameValid(updatedName)) {
            CommonMethods.displaySnackbar(view, R.string.error_length_name);
        } else if (!CommonMethods.isPasswordValid(updatedPassword)) {
            CommonMethods.displaySnackbar(view, R.string.error_length_password);
        } else if (!CommonMethods.isContactValid(updatedContact)) {
            CommonMethods.displaySnackbar(view, R.string.error_invalid_contact);
        } else {

            /*If the details entered by the customer are valid, the updated details are
              put into the HashMap called updatedDetails.*/
            updatedDetails.put("name", updatedName);
            updatedDetails.put("password", updatedPassword);
            updatedDetails.put("email", updatedEmail);
            updatedDetails.put("contact", updatedContact);
            updatedDetails.put("customerId", customerId);

            /*Once the details are assigned into the HashMap, the UpdateCustomerTask is executed by passing the
              updatedDetails HashMap.*/
            new UpdateCustomerTask(view).execute(updatedDetails);
        }
    }

    /* Actions that take place once the "UPDATE" button is clicked */
    public void btnUpdateClicked(View view) {
        /*The updated values entered by the user are retrieved, by omitting the white spaces at the beginning
        * and at the end of the data, and assigned to String variables.*/
        updatedName = name.getText().toString().trim();
        updatedEmail = email.getText().toString().trim();
        updatedContact = contact.getText().toString().trim();
        updatedPassword = password.getText().toString().trim();

        validate();//the validate() method is called in order to validate the retrieved data
    }


    @Override
    public void onTaskComplete(Customer result) {
        if (result != null) {
            customer = new Customer();
            customer = result;
            setValues();
        }
    }


    public void setValues() {
        name.setText(customer.getName());
        labelName.setText(customer.getName());
        contact.setText(customer.getContact());
        email.setText(customer.getEmail());
        password.setText(customer.getPassword());
    }

    //Unregister function
    //use for unregister users from bite hunter app
    //performing after permission granted
    private void unregisterUser() {

        //initialize alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCustomerActivity.this);
        //set alert dialog title
        builder.setTitle("Delete User Account");
        //set alert dialog message
        builder.setMessage("Are you sure ?");
        //set alert dialog can cancelable
        builder.setCancelable(true);
        //set positive button and add click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //calling and execute UnregisertUserTask background worker to do work
                //this will do send request to the url provided and delete user account using that url
                new UnregisterUserTask(view, customerId).execute();

            }
        });
        //set negative button for cancel the process
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create builder to alert dialog
        AlertDialog alertDialog = builder.create();
        //show alert dialog
        alertDialog.show();
    }

    //This method is used for session handling
    public SharedPreferences getSharedPrefs() {

        //the shared preferences object is retrieved
        //using this object, the application is able to access the details such as customerId,
        //customerName and customerEmail
        sharedpreferences = getSharedPreferences(CommonTags.MyPREFERENCES, Context.MODE_PRIVATE);

        //the customer ID of the user who logged in is retrieved
        //the customer ID is converted to a String variable
        //the customer ID is then assigned to a String variable called customerID
        customerId = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_ID, null);
        String customerName = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_NAME, null);//the same is done to the customerName
        String customerEmail = sharedpreferences.getString(CommonTags.TAG_CUSTOMER_EMAIL, null);//the same is done to the customerEmail

        //returns the sharedpreferences object
        return sharedpreferences;
    }


    public void btnUnregisterClicked(View view) {
        unregisterUser();
    }
}
