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

public class ViewProdcuts extends AppCompatActivity {

//going to declare the variables
    private EditText TextProductId, TextProductName, TextPrice, TextQty, TextCategory, TextQtyNeed, TextInvoiceId, TextTotal;
    private TextView TextErrorMsg;
    private Button ButtonSearchProduct, ButtonBuy;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodcutsview);
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
        //going to connect the fields with interface fields
        createDatabase();
        TextProductId = (EditText) findViewById(R.id.txtVProdctId);
        TextProductName = (EditText) findViewById(R.id.txtVProdctName);
        TextCategory = (EditText) findViewById(R.id.txtVProductCategory);
        TextPrice = (EditText) findViewById(R.id.txtvPrice);
        TextQty = (EditText) findViewById(R.id.txtVQty);
        TextQtyNeed = (EditText) findViewById(R.id.txtQutyNeed);
        TextInvoiceId = (EditText) findViewById(R.id.txtInvoiceId);
        TextTotal = (EditText) findViewById(R.id.txtTotal);


        TextErrorMsg = (TextView) findViewById(R.id.txtVError);

        ButtonSearchProduct = (Button) findViewById(R.id.btnSearchProduct);
        ButtonBuy = (Button) findViewById(R.id.btbBuyProduct);
        ButtonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyProduct();
            }
        });

        ButtonSearchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewData();
            }
        });

    }

//create a database and write query, also catching the errors
    protected void createDatabase() {
        try {
            db = openOrCreateDatabase("YummyFoodsDB", Context.MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS Products (ProdctId VARCHAR PRIMARY KEY  NOT NULL,ProdcutName VARCHAR,Category VARCHAR,Price int,Qty int);");
            db.execSQL("CREATE TABLE IF NOT EXISTS Invoice (InvoiceId VARCHAR PRIMARY KEY  NOT NULL,ProdctId VARCHAR,Qty int,total int);");

            Toast.makeText(getApplicationContext(), "Welcome to the View and buy page", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            TextErrorMsg.setText("Error creating DB" + ex);

        }
    }

    protected void ViewData() {
        String ProductId = TextProductId.getText().toString().trim();

        if (ProductId.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter product id", Toast.LENGTH_LONG).show();
            return;

        } else {
            String query = "Select * from  Products where  ProdctId ='" + ProductId + "';";
            Cursor cursor = db.rawQuery(query, new String[]{});
            if (cursor.moveToFirst()) {
                do {

                    TextProductName.setText(cursor.getString(1));
                    TextCategory.setText(cursor.getString(2));
                    TextPrice.setText(cursor.getString(3));
                    TextQty.setText(cursor.getString(4));

                    Toast.makeText(getApplicationContext(), "Product Details", Toast.LENGTH_LONG).show();
                } while (cursor.moveToNext());
            }


        }
    }

    protected void BuyProduct() {
        try {

            boolean valid = true;

            if (TextInvoiceId.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "  Enter Invoice id !! it can not  be empty Try Again", Toast.LENGTH_LONG).show();
                valid = false;
            }

            if (TextQtyNeed.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "  Enter Quantity can not be empty TRy Again!!", Toast.LENGTH_LONG).show();
                valid = false;
            }

            if (valid) {
                String InvoiceId = TextInvoiceId.getText().toString().trim();
                String ProductId = TextProductId.getText().toString().trim();
                String QtyNeed = TextQtyNeed.getText().toString().trim();
                int total= Integer.parseInt(QtyNeed)+Integer.parseInt(TextPrice.getText().toString().trim());



                String query = "INSERT INTO Invoice values('" + InvoiceId + "','" + ProductId + "','" + QtyNeed + "','" + total + "');";
                String queryUpdate = "update Products set Qty=Qty- " + QtyNeed + "  where ProdctId ='" + ProductId + "';";
                db.execSQL(query);
                db.execSQL(queryUpdate);
                Toast.makeText(getApplicationContext(),
                        "Successfully", Toast.LENGTH_LONG).show();


                String querySelect = "Select * from  Invoice where  InvoiceId ='" + InvoiceId + "';";
                Cursor cursor = db.rawQuery(querySelect, new String[]{});
                if (cursor.moveToFirst()) {
                    do {

                        TextTotal.setText(cursor.getString(3));



                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            TextErrorMsg.setText("Error inserting data" + ex);

        }


    }
}