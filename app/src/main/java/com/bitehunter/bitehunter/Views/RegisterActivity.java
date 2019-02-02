package com.bitehunter.bitehunter.Views;

/**
 * Created by Yash on 11/22/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitehunter.bitehunter.Helper.RegisterCustomerTask;
import com.bitehunter.bitehunter.Others.CommonMethods;
import com.bitehunter.bitehunter.Others.JSONParser;
import com.bitehunter.bitehunter.Others.RemoteURL;
import com.bitehunter.bitehunter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    String u_username, u_password, u_repassword, u_email, u_contact;

    JSONParser jsonParser = new JSONParser();

    //database connection for the adding customer with the remote server
    private static final String url = RemoteURL.addCustomer;
    private static final String TAG_SUCCESS = "success";

    //declaring the xml onto the activity
    TextView txtAlreadyHave;
    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputReEnterPassword;
    private EditText inputEmail;
    private EditText inputContact;

    View view;
    //Creating a new has map to put values into an array
    HashMap<String, String> customerDetails = new HashMap<String, String>();

    //functions to execute on the start up of Activity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        view = getWindow().getDecorView().findViewById(android.R.id.content);

        //declaring the inputs to make valid text boxes for the user to enter details
        inputUsername = (EditText) findViewById(R.id.inputUser);
        inputPassword = (EditText) findViewById(R.id.inputPassworda);
        inputReEnterPassword = (EditText) findViewById(R.id.inputRepassword);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputContact = (EditText) findViewById(R.id.inputContact);
        txtAlreadyHave = (TextView) findViewById(R.id.txtAlreadyHave);


        //redirecting to the login page if customer already has an account
        txtAlreadyHave.setOnClickListener(new View.OnClickListener() {

            //what the activity will do upon registration button is clicked
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    //the registratioon function with all the functionality
    public void excuteRegister() {

        //getting the user details to push to the database
        u_username = inputUsername.getText().toString();
        u_password = inputPassword.getText().toString();
        u_repassword = inputReEnterPassword.getText().toString();
        u_email = inputEmail.getText().toString();
        u_contact = inputContact.getText().toString();

        //checking for blank fields
        if (u_username.isEmpty() || u_password.isEmpty() || u_repassword.isEmpty() || u_email.isEmpty() || u_contact.isEmpty()) {

            // if the above statement is true and fields are blank then the below error message will show
            Toast.makeText(getBaseContext(), "You cannot leave blank fields!", Toast.LENGTH_LONG).show();

        } else {
            //checking whether the user typed password is equal to the re-type password
            if (u_repassword.equals(u_password)) {
                //if all the conditions are passed and successful then the fata will be added onto the database
                customerDetails.put("name", u_username);
                customerDetails.put("password", u_password);
                customerDetails.put("email", u_email);
                customerDetails.put("contact", u_contact);

                new RegisterCustomerTask(view).execute(customerDetails);
            }
            //if the passwords does not match the below message will pop as and error
            else {
                CommonMethods.displaySnackbar(view, "Passwords do not match!");

            }
        }
    }
    //calling the excuteRegister method when the button is clicked
    public void btnCreateAccountClicked(View view) {

        excuteRegister();
    }
}

