package com.example.tigermartdepauw.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tigermartdepauw.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    Toolbar toolbar;
    TextView subtotal, discount, shipping, total;
    Button paymentButton, discountButton;
    double amount= 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar= findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        amount= getIntent().getDoubleExtra("amount",0.0);


        subtotal= findViewById(R.id.sub_total);
        discount= findViewById(R.id.textView17);
        shipping= findViewById(R.id.textView18);
        total= findViewById(R.id.total_amt);
        paymentButton=findViewById(R.id.pay_btn);
        discountButton= findViewById(R.id.discount_button);

        subtotal.setText(amount+" $");
        total.setText(amount+ " $");
        
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod();
            }


        });

        discountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount= getIntent().getDoubleExtra("amount",0.0);
                double discountAmount= 0.7 * amount;
                discount.setText(discountAmount+ " $");
                subtotal.setText(amount+" $");
                amount= amount- discountAmount;
                total.setText(amount+ " $");


                Toast.makeText(PaymentActivity.this, "Black Friday Deal! 70% off discount added", Toast.LENGTH_SHORT).show();




            }
        });






    }

    private void paymentMethod() {

        Checkout checkout = new Checkout();

        final Activity activity = PaymentActivity.this;

        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "My E-Commerce App");
            //Ref no
            options.put("description", "Reference No. #123456");
            //Image to be display
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            // Currency type
            options.put("currency", "USD");
            //double total = Double.parseDouble(mAmountText.getText().toString());
            //multiply with 100 to get exact amount in rupee
            amount = amount * 100;
            //amount
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("email", "developer.aryan@gmail.com");
            //contact
            preFill.put("contact", "1234557");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
    }
}

