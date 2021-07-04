package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//declare the variables
    EditText TextUserId,TextPassword;
    Button ButtonLogin,ButtonRegister;
    SQLiteDatabase db;
    private TextView TextErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        createDatabase();
        TextUserId = (EditText) findViewById(R.id.txtLUserId);
        TextPassword = (EditText) findViewById(R.id.txtLPassword);


        TextErrorMsg = (TextView) findViewById(R.id.txtError);
        ButtonLogin = (Button) findViewById(R.id.btnLogin);
        ButtonRegister = (Button) findViewById(R.id.btnLRegister);

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateLogin();
            }
        });

        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startNewActivity=new Intent(MainActivity.this,Register.class);
                startActivity(startNewActivity);
            }
        });
    }

//create the database and going to enter the data
    protected void createDatabase() {
        try {
            db = openOrCreateDatabase("YummyFoodsDB", Context.MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS ResgisterUser (UserId VARCHAR PRIMARY KEY  NOT NULL,Password VARCHAR,UserType VARCHAR);");

            Toast.makeText(getApplicationContext(), "Welcome To The yummy foods!!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            TextErrorMsg.setText("Error creating DB" + ex);

        }
    }
//going to create a login and select the users using trycatch method catch the errors
    protected void ValidateLogin() {
        try {
            String UserType=null;
            String userName = TextUserId.getText().toString().trim();
            String password = TextPassword.getText().toString().trim();
            if (userName.isEmpty() ||password.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        " Enter the Correct Details Please!!", Toast.LENGTH_LONG).show();
            } else {
                String query = "Select * from  ResgisterUser where  UserId ='" + userName + "' and Password ='" + password + "';";
                Cursor cursor = db.rawQuery(query, new String[]{});
                if (cursor.moveToFirst()) {
                    do {

                        UserType=cursor.getString(2);
                    } while (cursor.moveToNext());
                    if(UserType.equals("Admin")) {
                        Toast.makeText(getApplicationContext(), "Admin ", Toast.LENGTH_LONG).show();
                        Intent startNewActivity=new Intent(MainActivity.this,AdminHome.class);
                        startActivity(startNewActivity);
                    }
                    if(UserType.equals("Customer")) {
                        Toast.makeText(getApplicationContext(), "Customer", Toast.LENGTH_LONG).show();
                        Intent startNewActivity=new Intent(MainActivity.this,MemberHome.class);
                        startActivity(startNewActivity);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Plese Enter the valid information ", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception ex)
        {
            TextErrorMsg.setText("Error Searching data"+ex);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
