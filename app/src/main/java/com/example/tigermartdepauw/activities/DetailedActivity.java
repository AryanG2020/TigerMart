package com.example.tigermartdepauw.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;





import com.bumptech.glide.Glide;
import com.example.tigermartdepauw.R;
import com.example.tigermartdepauw.models.NewProductModel;
import com.example.tigermartdepauw.models.PopularProductsModel;
import com.example.tigermartdepauw.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg, addItems, removeItems;
    TextView name, description, price, quantity;
    Button addToCart,  buyNow;
    int totalQuantity= 1;
    double totalPrice=0;
    Toolbar toolbar;

    private FirebaseFirestore firestore;
    NewProductModel newProductModel= null;
    PopularProductsModel popularProductsModel=null;

    ShowAllModel showAllModel= null;

    FirebaseAuth auth;
   // private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        toolbar=findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        final Object obj=getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductModel)
        {
            newProductModel= (NewProductModel) obj;
        }else if (obj instanceof  PopularProductsModel){
            popularProductsModel=(PopularProductsModel) obj;
        } else if (obj instanceof  ShowAllModel){
            showAllModel=(ShowAllModel) obj;
        }

        detailedImg=findViewById(R.id.detailed_img);
        quantity=findViewById(R.id.quantity);
        addItems=findViewById(R.id.add_item);
        removeItems=findViewById(R.id.remove_item);
        name=findViewById(R.id.detailed_name);
        description=findViewById(R.id.detailed_desc);
        price=findViewById(R.id.detailed_price);
        addToCart=findViewById(R.id.add_to_cart);
        buyNow=findViewById(R.id.buy_now);

        //New Products
        if (newProductModel !=null)
        {
            Glide.with(getApplicationContext()).load(newProductModel.getImg_url()).into(detailedImg);
            name.setText(newProductModel.getName());
            description.setText(newProductModel.getDescription());
            price.setText(String.valueOf(newProductModel.getPrice()));

            totalPrice= newProductModel.getPrice() * totalQuantity;

        }

        //popular products

        if (popularProductsModel !=null)
        {
            Glide.with(getApplicationContext()).load(popularProductsModel.getImg_url()).into(detailedImg);
            name.setText(popularProductsModel.getName());
            description.setText(popularProductsModel.getDescription());
            price.setText(String.valueOf(popularProductsModel.getPrice()));

            totalPrice= popularProductsModel.getPrice() * totalQuantity;

        }

        //show all products

        if (showAllModel !=null)
        {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));

            totalPrice= showAllModel.getPrice() * totalQuantity;

        }




    //add to cart
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity<10)
                {
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductModel !=null){

                        totalPrice= newProductModel.getPrice() * totalQuantity;
                    }
                    if (popularProductsModel !=null){

                        totalPrice= popularProductsModel.getPrice() * totalQuantity;
                    }
                    if (showAllModel !=null){

                        totalPrice= showAllModel.getPrice() * totalQuantity;
                    }

                }

            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity>1)
                {
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }


            }
        });

        //BUY NOW

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DetailedActivity.this, AddressActivity.class);

                if (newProductModel!= null)
                {
                    intent.putExtra("item", newProductModel);
                    totalPrice= newProductModel.getPrice() * totalQuantity;
                    // intent.putExtra("totalPrice", totalPrice);

                }
                if (popularProductsModel!=null)
                {
                    intent.putExtra("item", popularProductsModel);
                    totalPrice= popularProductsModel.getPrice() * totalQuantity;
                    // intent.putExtra("totalPrice", totalPrice);

                }
                if (showAllModel!=null)
                {
                    intent.putExtra("item", showAllModel);
                    totalPrice= showAllModel.getPrice() * totalQuantity;
                   // intent.putExtra("totalPrice", totalPrice);

                }

               intent.putExtra("totalPrice", totalPrice);
                startActivity(intent);

            }
        });



    }



    private void addToCart() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap= new HashMap<>();
        cartMap.put("productName", name.getText().toString() );
        cartMap.put("productPrice", price.getText().toString() );
        cartMap.put("currentTime", saveCurrentTime );
        cartMap.put("currentDate", saveCurrentDate );
        cartMap.put("totalQuantity", quantity.getText().toString() );
        cartMap.put("totalPrice", totalPrice );

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });






    }
}












