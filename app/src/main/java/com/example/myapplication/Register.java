package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
//declare the variables
    private EditText TextUserId, TextPassword, TextConfirmPassword;
    private TextView TextErrorMsg;
    private Spinner SpinnerUserType;
    private Button ButtonRegister;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//going to conect the fields with database
        createDatabase();
        TextUserId = (EditText) findViewById(R.id.txtRUserId);
        TextPassword = (EditText) findViewById(R.id.txtRPassowrd);
        TextConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);

        TextErrorMsg = (TextView) findViewById(R.id.txtRError);
        ButtonRegister = (Button) findViewById(R.id.btnRegister);

//select the usertype
        SpinnerUserType = (Spinner) findViewById(R.id.spUserType);
        List<String> list = new ArrayList<>();
        list.add("Admin");
        list.add("Customer");

        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerUserType.setAdapter(dataAdapter);

        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertIntoDb();
            }
        });
    }

//create the database called yummy foods, also please be concern with database queries
    protected void createDatabase() {
        try {
            db = openOrCreateDatabase("YummyFoodsDB", Context.MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS ResgisterUser (UserId VARCHAR PRIMARY KEY  NOT NULL,Password VARCHAR,UserType VARCHAR);");

            Toast.makeText(getApplicationContext(), "Welcome To The yummy foods!!!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            TextErrorMsg.setText("Error creating DB" + ex);

        }
    }

//going to do the validation part and also catching the errors in the system
    protected void insertIntoDb() {
        try {

            boolean valid = true;

            if (TextUserId.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "User Name or ID cannot be empty!!!", Toast.LENGTH_LONG).show();
                valid = false;
            }
            
            if (TextPassword.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Password cannot be empty?!", Toast.LENGTH_LONG).show();
                valid = false;
            }
            
            if (!TextPassword.getText().toString().isEmpty() && TextPassword.getText().toString().length() < 3) {
                Toast.makeText(getApplicationContext(), "Password must have a minimum of 3 characters!!", Toast.LENGTH_LONG).show();
                valid = false;
            }
            
            if (TextConfirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Confirm Password cannot be empty!!", Toast.LENGTH_LONG).show();
                valid = false;
            }
            
            if (!TextPassword.getText().toString().equals(TextConfirmPassword.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Wrong Passwords, please try again!!", Toast.LENGTH_LONG).show();
                valid = false;
            }


            if (valid) {
                String UserName = TextUserId.getText().toString().trim();
                String Password = TextPassword.getText().toString().trim();
                String UserType = SpinnerUserType.getSelectedItem().toString().trim();


                String query = "INSERT INTO ResgisterUser values('" + UserName + "','" + Password + "','" + UserType + "');";
                db.execSQL(query);
                Toast.makeText(getApplicationContext(),
                        "Resgister User Successfully welcome to the Yummy Foods", Toast.LENGTH_LONG).show();


            }

        } catch (Exception ex) {
            TextErrorMsg.setText("Error inserting data" + ex);

        }

    }
}
