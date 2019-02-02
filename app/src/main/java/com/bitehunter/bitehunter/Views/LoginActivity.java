package com.bitehunter.bitehunter.Views;

/**
 * Created by Yash on 10/20/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitehunter.bitehunter.Helper.AsyncTaskCompleteListener;
import com.bitehunter.bitehunter.Helper.CustomerDetailsTask;
import com.bitehunter.bitehunter.Helper.LoginCustomerTask;
import com.bitehunter.bitehunter.Model.Customer;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.CommonTags;
import com.bitehunter.bitehunter.Others.JSONParser;
import com.bitehunter.bitehunter.Others.RemoteURL;
import com.bitehunter.bitehunter.R;

import org.w3c.dom.Text;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements AsyncTaskCompleteListener<HashMap> {

    private EditText inputEmail, inputPassword;
    TextView txtCreateNew, txtForgotPass;
    View view;
    HashMap<String, String> loginDetails = new HashMap<>();


    HashMap<String, String> customerDetails = new HashMap<>();

    //introducing shared preferences
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(CommonTags.MyPREFERENCES, Context.MODE_PRIVATE);

        //keep me logged in function calling
        keepMeLoggedIn();

        //declaring links to go to other forms
        //clicking on these will direct the users into particular forms(in here to the register activity)
        txtCreateNew = (TextView) findViewById(R.id.txtCreateNew);
        txtCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        //calling initViews method
        initViews();
    }

    //keep me logged in function
    //customer will kept logged in through out the application functions
    private void keepMeLoggedIn() {
        //check customer name is available in shared preference
        if (getCustomerName().length() != 1) {
            //intent Home Activity if customer name is available in shared preference
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            //starting activity
            startActivity(intent);
            //remove this activity from back stack (back button press will exit the application)
            finish();
        }
    }

    //keep me logged in function calling when application resuming
    @Override
    protected void onResume() {
        super.onResume();
        //calling keepMeLoggedIn function in resume activity life circle
        //when user went to the another app and com back to the this app this onResume method will trigger.
        //then application starting with login activity
        // but because of this function calling it will show home activity to the customer
        keepMeLoggedIn();
    }

    //putting the variables into the initViews method
    //each text box is declared with the .xml fields
    public void initViews() {
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        view = (LinearLayout) findViewById(R.id.layoutLogin);

    }

    //functions to do after the login button is clicked is been implemented inside this method
    public void btnLoginClicked(View v) {

        //variables that takes in user input inorder to check availability on database
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        //validations for the text inputs
        //if the  fields are empty an error message will be shown!
        if (email.isEmpty() || password.isEmpty()) {
            CommonMethods.displaySnackbar(view, "Empty Fields!");
            //validation for the email format in order for the user to use the correct symbols in typing the email
        } else if (!CommonMethods.isEmailValid(email)) {
            CommonMethods.displaySnackbar(view, "Incorrect Email Format!");
            //calidations for the password from the common methods are called here. the password length will be checked
        } else if (!CommonMethods.isPasswordValid(password)) {
            CommonMethods.displaySnackbar(view, "Incorrect Password!");

        } else {
            //if the validations are passed the customer details will be put into the database.
            loginDetails.put("email", email);
            loginDetails.put("password", password);

            new LoginCustomerTask(view).execute(loginDetails);
        }

    }
    @Override
    public void onTaskComplete(HashMap result) {
//passing the result of the method execution
        customerDetails = result;

        //getting details into shared preferences
        if (customerDetails != null) {
            SharedPreferences.Editor editor = sharedpreferences.edit();

            //retrieving the name, email and id
            String customerName = customerDetails.get(CommonTags.TAG_CUSTOMER_NAME);
            String customerEmail = customerDetails.get(CommonTags.TAG_CUSTOMER_EMAIL);
            String customerId = customerDetails.get(CommonTags.TAG_CUSTOMER_ID);

            //declaring the name, email and id into the editor
            editor.putString(CommonTags.TAG_CUSTOMER_NAME, customerName);
            editor.putString(CommonTags.TAG_CUSTOMER_EMAIL, customerEmail);
            editor.putString(CommonTags.TAG_CUSTOMER_ID, customerId);
            //committing the editor
            editor.commit();

            //pointing out the activities to be used next
            Intent HomeActivity = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(HomeActivity);

        } else {
            //implementations for this is been made in the commonMethods
            CommonMethods.displaySnackbar(view, "Invaild Credentials!");
        }
    }

    //getting shared preferences from the login activity
    public String getCustomerName() {
        //return customer name that holds on shared preference
        return sharedpreferences.getString(CommonTags.TAG_CUSTOMER_NAME, "0");
    }

}