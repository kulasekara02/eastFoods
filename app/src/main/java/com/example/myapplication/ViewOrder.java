package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewOrder extends AppCompatActivity {
//declare the variables
    private EditText TextInvoiceId, TextProductId, TextQty, TextTotal;
    private TextView TextErrorMsg;
    private Button ButtonSearchInvoice ;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderview);
     //   Toolbar toolbar = findViewById(R.id.toolbar);
     //   setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        createDatabase();
        TextInvoiceId = (EditText) findViewById(R.id.txtVIInvoiceId);
        TextProductId = (EditText) findViewById(R.id.txtVIProductID);
        TextQty = (EditText) findViewById(R.id.txtIVQuantity);
        TextTotal = (EditText) findViewById(R.id.txtVItotal);

        TextErrorMsg = (TextView) findViewById(R.id.txtIError);

        ButtonSearchInvoice = (Button) findViewById(R.id.btnISearchOrder);

        ButtonSearchInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewData();
            }
        });
    }


//creating the database called yummy foods and writing a query
    protected void createDatabase() {
        try {
            db = openOrCreateDatabase("YummyFoodsDB", Context.MODE_PRIVATE, null);


            db.execSQL("CREATE TABLE IF NOT EXISTS Invoice (InvoiceId VARCHAR PRIMARY KEY  NOT NULL,ProdctId VARCHAR,Qty int,total int);");

            Toast.makeText(getApplicationContext(), "Welcome PLz View Your Order", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            TextErrorMsg.setText("Error creating DB" + ex);

        }
    }

    protected void ViewData() {
        String InvoiceId = TextInvoiceId.getText().toString().trim();

        if (InvoiceId.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter your Invoice id !!", Toast.LENGTH_LONG).show();
            return;

        } else {
            String query = "Select * from  Invoice where  InvoiceId ='" + InvoiceId + "';";
            Cursor cursor = db.rawQuery(query, new String[]{});
            if (cursor.moveToFirst()) {
                do {

                    TextProductId.setText(cursor.getString(1));
                    TextQty.setText(cursor.getString(2));
                    TextTotal.setText(cursor.getString(3));


                    Toast.makeText(getApplicationContext(), " This is Your Invoice !!", Toast.LENGTH_LONG).show();
                } while (cursor.moveToNext());
            }
        }
    }
}
